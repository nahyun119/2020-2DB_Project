const mysqlUtil = require('../../config/mysqlUtil');

module.exports = function (req, res) {
    const _funcName = arguments.callee.name;

    try{
        let param = req.body;
        req.paramBody = param;

        checkParameter(param, 'customer_id');
        checkParameter(param, 'account_type');

        mysqlUtil.connectPool( async function (db_connection) {
            req.innerBody = {};

            req.innerBody['item'] = await queryUpdateAccount(req, db_connection);
            console.log(req.innerBody);
            if(!req.innerBody['item']){
                let err = new Error();
                err.code = '400';
                err.message = '요금제 가입을 실패하였습니다.';
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

function queryUpdateAccount(req, db_connection){
    return mysqlUtil.querySingle(db_connection
        , 'call proc_update_user_account_plan(?,?)'
        , [
            req.paramBody['customer_id'],
            req.paramBody['account_type']
        ]
    );
}