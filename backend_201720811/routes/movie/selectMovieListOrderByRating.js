const mysqlUtil = require('../../config/mysqlUtil');

module.exports = function (req, res) {
    const _funcName = arguments.callee.name;

    try{
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

function querySelectList(req, db_connection){
    return mysqlUtil.queryArray(db_connection
        , 'call proc_select_movie_list_order_by_rating()'
        , [
        ]
    );
}