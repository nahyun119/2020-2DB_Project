
const mysql = require('mysql');
const config = require('./mysqlConfig');

const pool= mysql.createPool( config.mysqlConfig );

let self;
self = module.exports = {

    connectPool: function(asyncFunc, errorHandler){
        pool.getConnection(async function (err, connection) {
            try {
                if (err) {
                    await errorHandler( err );
                } else {
                    await connection.beginTransaction(); // 트랜잭션 적용 시작
                    await asyncFunc(connection);
                    await connection.commit(); // 커밋
                }
            } catch ( e ) {
                connection.rollback();
                console.log(`Error connectPool e: ${e}`);
                console.log(`Error connectPool e.message: ${e.message}`);
                let _stack = (e instanceof Error) ? '' : (e.stack ? e.stack : e);
                let _msg = (e instanceof Error) ? e.message : e;
                let _code = (e instanceof Error) ? e.code : errCode.system;

                let error = new Error();
                error.code = _code;
                error.message = _msg;
                error.stack = _stack;
                let _err = (e instanceof Error) ? e : error;
                if( e.stack ){
                    console.log(`Error connectPool stack: ${e.stack}`);
                }
                await errorHandler( _err );
            } finally {
                if( connection.state !== 'disconnected' ){
                    connection.release();
                }
            }
        });
    },
    query: async function (db_connection, query, params, resultCallback) {
        if (db_connection && db_connection.state !== 'disconnected') {
            await db_connection.query(
                query,
                params,
                async function (err, rows, fields) {
                    if (err) {
                        let error = new Error();
                        error.code = '200';
                        error.message = err.message;
                        error.stack = err.stack;
                        await resultCallback(null, error);
                    } else {
                        await resultCallback(rows, null);
                    }
                });
        }
        else {
            let err = new Error();
            err.code = '200';
            err.msg = 'queryInfo is error - msg';
            err.stack = 'queryInfo is error - stack';

            await resultCallback(null, err);
        }
    },

    querySingle: function(db_connection, query, params) {
        const _funcName = arguments.callee.name;

        return new Promise(function (resolve, reject) {
            self.query(db_connection, query, params, function (rows, err) {
                if( err ){
                    reject(err);
                }
                else {
                    if( Array.isArray(rows) ){
                        resolve(rows[0][0]);
                    }
                    else {
                        resolve(null);
                    }
                }

            });
        });
    },

    queryArray: function(db_connection, query, params) {
        const _funcName = arguments.callee.name;

        return new Promise(function (resolve, reject) {
            self.query(db_connection, query, params, function (rows, err) {
                if( err ){
                    reject(err);
                }
                else {
                    resolve(rows[0]);
                }

            });
        });
    },

}
