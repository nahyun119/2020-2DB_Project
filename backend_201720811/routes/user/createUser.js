const mysqlUtil = require('../../config/mysqlUtil');

module.exports = function (req, res) {
    const _funcName = arguments.callee.name;

    try{
        let param = req.body;
        req.paramBody = param;

        checkParameter(param, 'last_name');
        checkParameter(param, 'first_name');
        checkParameter(param, 'email');
        checkParameter(param, 'account_number');
        checkParameter(param, 'password');
        checkParameter(param, 'account_id');


        mysqlUtil.connectPool( async function (db_connection) {
            req.innerBody = {};

            console.log(param);

            req.innerBody['item'] = await queryCreateUser(req, db_connection);
            console.log(req.innerBody);

            if(!req.innerBody['item']){
                let err = new Error();
                err.code = '400';
                err.message = '회원 가입을 실패하였습니다.';
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

function queryCreateUser(req, db_connection){
    return mysqlUtil.querySingle(db_connection
        , 'call proc_create_user(?,?,?,?,?,?)'
        , [
            req.paramBody['last_name'],
            req.paramBody['first_name'],
            req.paramBody['email'],
            req.paramBody['account_number'],
            req.paramBody['password'],
            req.paramBody['account_id'],
        ]
    );
}