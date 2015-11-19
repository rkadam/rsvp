/* jshint node:true */
'use strict';
var ldap = require('ldapjs');
var Q = require('q');

var client = ldap.createClient({
  url: 'ldaps://ldap.savagebeast.com:636'
});

client.bind('', '', function(err) {
  if (err) {
    console.error('ldap error', err);
  }
});

var ldapsearch = Q.nbind(client.search, client);

var depts = {};

var search = function(base, opts) {
  return ldapsearch(base, opts)
    .then(function(res) {
      var data = null;
      var deferred = Q.defer();
      res.on('searchEntry', function(res) {
        data = res.object;
      });
      res.on('error', function(err) {
        deferred.reject(err);
      });
      res.on('end', function(result) {
        deferred.resolve(data);
      });
      return deferred.promise;
    }).fail(function(err) {
      console.log('error!', err);
    });
};

var userSearch = function(uid) {
  var opts = {
    filter: "(|(uid="+uid+")(RFC822MailMember="+uid+"))",
    attributes: ['uid', 'pandoraStartDate', 'ou', 'displayName'],
    scope: 'sub'
  };
  var base = 'dc=savagebeast,dc=com';
  return search(base, opts).then(function(res) {
    return {
      uid: res.uid,
      name: res.displayName,
      years: ((Date.now() - Date.parse(res.pandoraStartDate)) / (1000 * 60 * 60 * 24 * 365)).toPrecision(3),
      department: res.ou,
    };
  });
};

var updateDepartments = function() {
  var opts = {

  };
};
// userSearch('gmichalec')
//   .then(function(res) {
//     console.log(res);
//   }).done();

//   userSearch('gregm')
//     .then(function(res) {
//       console.log(res);
//     }).done();

// search('dc=savagebeast,dc=com', opts);
// var opts = {
//   // filter: "(ou=1205)",
//   // filter: '(cn=departments)',
//   filter: '(pandoraListMember=*1205)',
//   scope: 'sub',
// };
// search('ou=sbldap,dc=savagebeast,dc=com', opts);

module.exports = {
  userSearch: userSearch,
  close: function() {
    client.destroy();
  }
};
