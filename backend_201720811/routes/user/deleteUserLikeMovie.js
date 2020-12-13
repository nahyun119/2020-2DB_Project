const mysqlUtil = require('../../config/mysqlUtil');

module.exports = function (req, res) {
    const _funcName = arguments.callee.name;

    try{
        let param = req.body;
        req.paramBody = param;

        checkParameter(param, 'customer_id');
        checkParameter(param, 'movie_id');

        mysqlUtil.connectPool( async function (db_connection) {
            req.innerBody = {};

            req.innerBody['list'] = await queryDelete(req, db_connection);
            console.log(req.innerBody);
            if(!req.innerBody['list']){
                let err = new Error();
                err.code = '400';
                err.message = '좋아하는 영화 삭제를 실패하였습니다.';
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

function queryDelete(req, db_connection){
    return mysqlUtil.queryArray(db_connection
        , 'call proc_delete_user_liked_movie(?,?)'
        , [
            req.paramBody['customer_id'],
            req.paramBody['movie_id'],
        ]
    );
}