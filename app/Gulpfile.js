"user strict";
var gulp      = require('gulp'),
  livereload  = require('gulp-livereload'),
  nodemon     = require('gulp-nodemon');

var paths = {
  frontendScripts: ['!client/vendor/**/*', 'client/**/*.js', 'client/**/*.html'],
  scripts: ['server/**/*.js', 'worker/**/*.js', 'test/**/*.js', 'common/**/*.js'],
  css: ['client/**/*.css'],
  views: ['!client/lib/*.html', 'client/**/*.html', 'client/index.html']
};

gulp.task('live', function () {
  livereload.listen(function (err) {
    if (err) {
      return console.error(err);
    } else {
      console.log('live reload listening');
    }
  });

  gulp.watch(paths.frontendScripts, function(event) {
    gulp.src(paths.frontendScripts).pipe(livereload());
  });
  gulp.watch(paths.css, function(event) {
    gulp.src(paths.css).pipe(livereload());
  });
});

gulp.task('serve', ['live'], function () {
  return nodemon({script: './app.js', ignore: ['client/**/*.js', 'worker/**/*.js', 'node_modules/**/*.js']})
    .on('restart', function () {
      livereload();
    });
});

gulp.task('default', ['serve'], function() {
});

