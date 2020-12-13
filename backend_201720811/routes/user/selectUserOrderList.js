const mysqlUtil = require('../../config/mysqlUtil');

module.exports = function (req, res) {
    const _funcName = arguments.callee.name;

    try{
        let param = req.query;
        req.paramBody = param;

        checkParameter(param, 'customer_id');

        mysqlUtil.connectPool( async function (db_connection) {
            req.innerBody = {};

            req.innerBody['list'] = await querySelectList(req, db_connection);
            console.log(req.innerBody);
            res.send(req.innerBody);


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

function querySelectList(req, db_connection){
    return mysqlUtil.queryArray(db_connection
        , 'call proc_select_user_order_list(?)'
        , [
            req.paramBody['customer_id'],
        ]
    );
}