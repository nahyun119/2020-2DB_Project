const mysqlUtil = require('../../config/mysqlUtil');

module.exports = function (req, res) {
    const _funcName = arguments.callee.name;

    try{
        let param = req.body;
        req.paramBody = param;

        checkParameter(param, 'customer_id');
        checkParameter(param, 'order_id');
        checkParameter(param, 'rating');

        mysqlUtil.connectPool( async function (db_connection) {
            req.innerBody = {};

            req.innerBody['item'] = await queryUpdateOrder(req, db_connection);
            console.log(req.innerBody);
            if(!req.innerBody['item']){
                let err = new Error();
                err.code = '400';
                err.message = '영화 반납을 실패하였습니다.';
                res.send(err);
                return;
            }

            req.innerBody['item']['movie_info'] = await queryUpdateRating(req, db_connection);
            if(!req.innerBody['item']['movie_info']){
                let err = new Error();
                err.code = '400';
                err.message = '영화 평점 업데이트를 실패하였습니다.';
                res.send(err);
                return;
            }
            console.log(req.innerBody);
            res.send(req.innerBody);

            // logUtil.printUrlLog(req, `item: ${JSON.stringify(req.innerBody['item'])}`);

        }, function (err) {
            res.send(err);
        } );

    }
    catch (e) {
        res.send(e);
    }
}

function checkParameter(param, key){
    console.log(param);
    if(!param[key]){
        let err = new Error();
        err.code = '400';
        err.message = `${key} 파라미터 오류`;
        throw err;
    }
}

function queryUpdateOrder(req, db_connection){
    return mysqlUtil.querySingle(db_connection
        , 'call proc_update_order(?,?)'
        , [
            req.paramBody['customer_id'],
            req.paramBody['order_id']
        ]
    );
}

function queryUpdateRating(req, db_connection){
    return mysqlUtil.querySingle(db_connection
        , 'call proc_update_movie_rating(?,?,?)'
        , [
            req.paramBody['order_id'],
            req.paramBody['rating'],
            req.innerBody['item']['movie_id'],
        ]
    );
}