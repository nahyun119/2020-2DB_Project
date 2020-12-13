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

            let check = await queryOrderCheck(req, db_connection);
            console.log(check);
            if(Number(check['available']) === 0 ){
                let err = new Error();
                err.code = '400';
                err.message = '현재 고객님의 영화 대여는 불가능합니다.';
                res.status(400).send(err);
                return;
            }

            req.innerBody['item'] = await queryCreateOrder(req, db_connection);
            console.log(req.innerBody);
            if(!req.innerBody['item']){
                let err = new Error();
                err.code = '400';
                err.message = '영화 대여를 실패하였습니다.';
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


function queryOrderCheck(req, db_connection){
    return mysqlUtil.queryArray(db_connection
        , 'select func_select_order_is_available(?) as available'
        , [
            req.paramBody['customer_id'],
        ]
    );
}

function queryCreateOrder(req, db_connection){
    return mysqlUtil.querySingle(db_connection
        , 'call proc_create_order(?,?)'
        , [
            req.paramBody['customer_id'],
            req.paramBody['movie_id']
        ]
    );
}