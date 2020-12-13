const mysqlUtil = require('../../config/mysqlUtil');

module.exports = function (req, res) {
    const _funcName = arguments.callee.name;

    try{
        let param = req.body;
        req.paramBody = param;

        checkParameter(param, 'id');
        checkParameter(param, 'password');

        mysqlUtil.connectPool( async function (db_connection) {
            req.innerBody = {};

            console.log("login")
            console.log(req);

            req.innerBody['item'] = await querySelectLogin(req, db_connection);
            if(!req.innerBody['item']){
                let err = new Error();
                err.code = '400';
                err.message = '로그인을 실패하였습니다.';
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

function querySelectLogin(req, db_connection){
    return mysqlUtil.querySingle(db_connection
        , 'call proc_select_user_login(?,?)'
        , [
            req.paramBody['id'],
            req.paramBody['password'],
        ]
    );
}