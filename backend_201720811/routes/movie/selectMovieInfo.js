const mysqlUtil = require('../../config/mysqlUtil');

module.exports = function (req, res) {
    const _funcName = arguments.callee.name;

    try{
        let param = req.query;
        req.paramBody = param;

        checkParameter(param, 'movie_id');

        mysqlUtil.connectPool( async function (db_connection) {
            req.innerBody = {};

            req.innerBody['item'] = await querySelectInfo(req, db_connection);
            console.log(req.innerBody);
            if(!req.innerBody['item']){
                let err = new Error();
                err.code = '400';
                err.message = '영화 정보가 없습니다.';
                res.send(err);
                return;
            }

            req.innerBody['item']['actor'] = await querySelectActorInfo(req, db_connection);

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


function querySelectInfo(req, db_connection){
    return mysqlUtil.querySingle(db_connection
        , 'call proc_select_movie_info(?)'
        , [
            req.paramBody['movie_id'],
        ]
    );
}

function querySelectActorInfo(req, db_connection){
    return mysqlUtil.queryArray(db_connection
        , 'call proc_select_actor_info_by_movie(?)'
        , [
            req.paramBody['movie_id'],
        ]
    );
}