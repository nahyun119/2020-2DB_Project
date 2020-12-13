const mysqlUtil = require('../../config/mysqlUtil');

module.exports = function (req, res) {
    const _funcName = arguments.callee.name;

    try{
        let param = req.query;
        req.paramBody = param;

        checkParameter(param, 'customer_id');

        mysqlUtil.connectPool( async function (db_connection) {
            req.innerBody = {};

            req.innerBody['item'] = await querySelectInfo(req, db_connection);
            console.log(req.innerBody);
            if(!req.innerBody['item']){
                let err = new Error();
                err.code = '400';
                err.message = '사용자 정보가 없습니다.';
                res.send(err);
                return;
            }
            let check = await queryAccountCheck(req, db_connection);
            req.innerBody['item']['is_expired'] = check['expired'];

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


function queryAccountCheck(req, db_connection){
    return mysqlUtil.queryArray(db_connection
        , 'select func_select_user_account_expired(?) as expired'
        , [
            req.paramBody['customer_id'],
        ]
    );
}

function querySelectInfo(req, db_connection){
    return mysqlUtil.querySingle(db_connection
        , 'call proc_select_user_info(?)'
        , [
            req.paramBody['customer_id'],
        ]
    );
}