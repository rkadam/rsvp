/* jshint node:true */
'use strict';

var autoprefixer = require('gulp-autoprefixer');
var concat = require('gulp-concat');
var gulp = require('gulp');
var less = require('gulp-less');
var minifycss = require('gulp-minify-css');
var paths = require('./paths.json');
var rimraf = require('rimraf');
var templateCache = require('gulp-angular-templatecache');

gulp.task('clean', function(callback) {
	rimraf(paths.dist, callback);
});

/**
 * Build
 */

gulp.task('build', [
	'build:js',
	'build:less',
	'build:html',
]);

gulp.task('build:js', [
	'build:js:vendor',
	'build:js:app',
]);

gulp.task('build:js:vendor', function() {
	buildJs(paths.js.vendor, 'vendor.js');
});

gulp.task('build:js:app', function() {
	buildJs(paths.js.app, 'app.js');
});

function buildJs(srcPaths, distFilename) {
	return gulp.src(srcPaths)
		.pipe(concat(distFilename))
		.pipe(gulp.dest(paths.dist));
}

gulp.task('build:less', function() {
	return gulp.src(paths.less)
		.pipe(less())
		.pipe(autoprefixer({
			browsers: [
				'last 2 Chrome versions',
				'last 2 Safari versions',
				'last 2 Firefox versions',
				'last 2 Explorer versions'
			],
			cascade: false,
			remove: true,
		}))
		.pipe(concat('all.min.css'))
		.pipe(minifycss())
		.pipe(gulp.dest(paths.dist));
});

gulp.task('build:html', function() {
	return gulp.src(paths.html)
		.pipe(templateCache('templates.js', { module: 'rsvp' }))
		.pipe(gulp.dest(paths.dist));
});

/**
 * Watch
 */

gulp.task('watch', [
	'watch:js',
	'watch:less',
	'watch:html',
]);

gulp.task('watch:js', ['build:js'], function() {
	var allJs = [].concat(paths.js.vendor, paths.js.app)
	return gulp.watch(allJs, ['build:js']);
});

gulp.task('watch:less', ['build:less'], function() {
	return gulp.watch(paths.src + '**/*.less', ['build:less']);
});

gulp.task('watch:html', ['build:html'], function() {
	return gulp.watch(paths.html, ['build:html']);
});
