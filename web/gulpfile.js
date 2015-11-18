/* jshint node:true */
'use strict';

var autoprefixer = require('gulp-autoprefixer');
var concat = require('gulp-concat');
var gulp = require('gulp');
var less = require('gulp-less');
var minifycss = require('gulp-minify-css');
var paths = require('./paths.json');
var rimraf = require('rimraf');

gulp.task('clean', function(callback) {
	rimraf(paths.dist.root, callback);
});

gulp.task('build', [
	'build:js',
	'build:less',
	// 'build:html',
]);

/**
 * build:js
 */

gulp.task('build:js', [
	'build:js:vendor',
	'build:js:app',
]);

gulp.task('build:js:vendor', function() {
	buildJs(paths.src.js.vendor, paths.dist.js.vendor);
});

gulp.task('build:js:app', function() {
	buildJs(paths.src.js.app, paths.dist.js.app);
});

function buildJs(srcPaths, distPath) {
	return gulp.src(srcPaths)
		.pipe(concat(distPath))
		.pipe(gulp.dest('.'));
}

/**
 * build:less
 */

gulp.task('build:less', function() {
	buildLess(paths.src.less.all, paths.dist.less.all);
});

function buildLess(srcPaths, distPath) {
	return gulp.src(srcPaths)
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
		.pipe(concat(distPath))
		.pipe(minifycss())
		.pipe(gulp.dest('.'));
}
