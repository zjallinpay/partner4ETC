//element 展示左边菜单栏; 预加载需要使用的模块
//由于layer弹层依赖jQuery，所以可以直接得到
var layer=null;
layui.use(['table', 'element', 'laypage', 'layer', 'form'], function () {
    var table = layui.table;
    var $ = layui.$;
    layer = layui.layer;
    var form = layui.form;

    //抽取查询方法
    var search = function () {
        table.render({
            //表格生成的位置：#ID
            elem: '#orginfotable',
            //请求地址getList
            url: '/manage/query/userhairpin/getList',
            //是否分页
            page: true,
            // toolbar: '#toolbarDemo', //开启头部工具栏，并为其绑定左侧模板
            // defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
            //     title: '提示'
            //     ,layEvent: 'LAYTABLE_TIPS'
            //     ,icon: 'layui-icon-tips'
            // }],
            //请求参数
            where: {
                authId: $("#useridquery").val(),//用户标识
                authName: $("#usernamequery").val(),//用户名称
                partnerId: $("#chanelidquery").val(),//机构编号
                orderNo: $("#serialnumberquery").val(),//流水号
                signsStatus: $("#chanetype").val(),//签约结果
                carNo:$("#querycarno").val(),//车牌号
                queryTimeStart: $("#creatdate").val() == "" ? "" : $("#creatdate").val().substr(0, 10),//查询创建时间起
                queryTimeEnd: $("#creatdate").val() == "" ? "" : $("#creatdate").val().substr(12, 11)//查询创建时间止
            },
            //分页信息
            request: {
                pageName: 'pageNum',
                limitName: 'pageSize'
            },

            //处理返回参数
            parseData: function (res) {

                if(res.data.total == 0) {
                            var index = layer.alert("无数据，请修改查询条件", function () {
                                layer.close(index);
                            })
                            return {
                                "code": res.code,
                                "msg": "无数据",
                                "count": res.data.total,
                                "data": res.data.list
                            };
                    }
                return {
                    "code": res.code,
                    "msg": res.msg,
                    "count": res.data.total,
                    "data": res.data.list
                };
            },

            //每页展示的条数
            limits: [5, 10,20],
            //每页默认显示的数量
            limit: 10,
            //单元格设置
            cols: [[
                // {field: 'partnerId', title: '机构编号'},
                // {field: 'authId',  title: '用户标识'},
                // {field: 'authName',  title: '用户名称'},
                // {field: 'phone',  title: '机构请求手机号'},
                // {field: 'orderNo',  title: '请求流水号'},
                // {field: 'reqtime',  title: '请求时间'},
                {field: 'partnername', title: '机构名称'},
                {field: 'carno',  title: '车牌号'},
                {field: 'licensecolor',  title: '车牌颜色'},
                {field: 'realphone',  title: '绑定手机号'},
                {field: 'id',  title: '车主身份证'},
                {field: 'realname',  title: '车主姓名'},
                {field: 'referer',  title: '推荐人'},
                {field: 'cardno',  title: '绑定银行卡号'},
                {field: 'serviceaddress',  title: '银行网点'},
                {field: 'issuestatus',  title: '发行状态'},
                {field: 'deliverymethod',  title: '邮寄方式'},
                {field: 'finishtime', title: '签约完成时间'}

            ]],

            done: function (res, curr, count) {

                // $("[data-field='deliverymethod']").children().each(function () {
                //     if ($(this).text() == '1') {
                //         $(this).text("快递")
                //     } else if ($(this).text() == '2') {
                //         $(this).text("自提")
                //     }
                // });
            }
        });
    };

    //页面加载就查询列表
    search();

    //条件查询
    $("#queryBtn").on("click", function () {
        // var index = layer.alert("立即提交", function () {
        //     layer.close(index);
        search();
        // })

    });

    //监听form表单提交事件 防止页面跳转
    form.on('submit(backbtn)', function (data) {
        return false;
    });

    form.on('submit(addFilter)', function (data) {
        return false;
    });


    //导出改为单独的事件，每次点击导出才会执行
    $("#export").click(function () {
        var ins1 = table.render({
            elem: '#data_export',
            url: '/manage/query/userhairpin/export', //数据接口
            method: 'get',
            title: '用户发行数据表',
            //请求参数
            where: {
                export: "1",
                authId: $("#useridquery").val(),//用户标识
                authName: $("#usernamequery").val(),//用户名称
                partnerId: $("#chanelidquery").val(),//机构编号
                orderNo: $("#serialnumberquery").val(),//流水号
                carNo:$("#querycarno").val(),//车牌号
                signsStatus: $("#chanetype").val(),//签约结果
                queryTimeStart: $("#creatdate").val() == "" ? "" : $("#creatdate").val().substr(0, 10),//查询创建时间起
                queryTimeEnd: $("#creatdate").val() == "" ? "" : $("#creatdate").val().substr(12, 11)//查询创建时间止
            },
            cols: [[
                // {field: 'partnerId', title: '机构编号'},
                // {field: 'authId', title: '用户标识'},
                // {field: 'authName', title: '用户名称'},
                {field: 'partnername', title: '机构名称'},
                {field: 'carno', title: '车牌号'},
                // {field: 'phone', title: '机构请求手机号'},
                // {field: 'orderNo', title: '请求流水号'},
                // {field: 'reqtime', title: '请求时间'},
                {field: 'licensecolor',  title: '车牌颜色'},
                {field: 'realphone', title: '绑定手机号'},
                {field: 'id', title: '车主身份证'},
                {field: 'realname', title: '车主姓名'},
                {field: 'referer',  title: '推荐人'},
                {field: 'cardno',  title: '绑定银行卡号'},
                {field: 'serviceaddress',  title: '银行网点'},
                {field: 'issuestatus', title: '发行状态'},
                {field: 'deliverymethod',  title: '邮寄方式'},
                {field: 'finishtime', title: '签约完成时间'}

            ]],
            done: function (res, curr, count) {
                // exportData = res.list;
                // table.exportFile(ins1.config.id, exportData, 'xls');
                var data = res.list;
                var excel = layui.excel;
                // console.log(res);
                // 重点！！！如果后端给的数据顺序和映射关系不对，请执行梳理函数后导出
                data = excel.filterExportData(data, [
                    // 'partnerId'
                    // , 'authId'
                    // , 'authName'
                    'partnername'
                     ,'carno'
                    // , 'phone'
                    // , 'orderNo'
                    // , 'reqtime'
                    ,'licensecolor'
                    , 'realphone'
                    , 'id'
                    , 'realname'
                    ,'referer'
                    ,'cardno'
                    ,'serviceaddress'
                    , 'issuestatus'
                    ,'deliverymethod'
                    , 'finishtime'
                ]);
                // 重点2！！！一般都需要加一个表头，表头的键名顺序需要与最终导出的数据一致
                data.unshift({
                    // partnerId: "机构编号",
                    // authId: "用户标识",
                    // authName: "用户名称",
                    // phone: "机构请求手机号",
                    // orderNo: "请求流水号",
                    // reqtime: "请求时间",
                    partnername:"机构名称",
                    carno: "车牌号",
                    licensecolor:"车牌颜色",
                    realphone: "绑定手机号",
                    id: "车主身份证",
                    realname: "车主姓名",
                    referer:"推荐人",
                    cardno:"绑定银行卡号",
                    serviceaddress: "银行网点",
                    issuestatus: "发行状态",
                    deliverymethod:"邮寄方式",
                    finishtime: "签约完成时间"
                });

                var timestart = Date.now();
                excel.exportExcel(data, '用户发行数据表.xlsx', 'xlsx');
                //	var timeend = Date.now();

                //	var spent = (timeend - timestart) / 1000;
                //	layer.alert('单纯导出耗时 '+spent+' s');

            }
        });
    })

});





