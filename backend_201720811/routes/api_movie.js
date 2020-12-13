
const express = require('express');
const app = express();

// 영화 평점 순 정렬
app.route('/list/rating').get(require('./movie/selectMovieListOrderByRating'));

// 영화 장르별 정렬
app.route('/list/genre').get( require('./movie/selectMovieListByGenre') );

// 영화 상세 정보(영화 정보 + 영화 상세 정보)
app.route('/info').get(require('./movie/selectMovieInfo'));

module.exports = app;
