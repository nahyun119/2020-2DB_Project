
const express = require('express');
const app = express();

// 사용자 회원가입
app.route('/signup').post(require('./user/createUser'));

// 사용자 로그인
app.route('/login').post(require('./user/selectUserLogin'));

// 사용자 정보
 app.route('/info').get(require('./user/selectUserInformation'));

// 사용자 요금제 가입하기
 app.route('/account').post(require('./user/createUserAccount') );

// 사용자 대여한 영화 목록
 app.route('/list/order').get(require('./user/selectUserOrderList'));

// 사용자 좋아하는 영화 목록
app.route('/list/movie').get(require('./user/selectUserLikeMovieList'));

// 사용자 좋아하는 영화 추가
app.route('/like/movie').post(require('./user/createUserLikeMovie'));

// 사용자 좋아하는 영화 삭제
app.route('/like/movie').delete(require('./user/deleteUserLikeMovie'));

// 사용자 영화 대여하기
app.route('/order').post(require('./user/createUserOrder'));

//사용자 대여한 영화 반납하기
app.route('/order').put(require('./user/updateUserOrder'));

module.exports = app;
