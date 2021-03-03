//element 展示左边菜单栏; 预加载需要使用的模块
//由于layer弹层依赖jQuery，所以可以直接得到
layui.use(['table', 'element', 'layer', 'form'], function () {
    var table = layui.table;
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;



    //抽取查询方法
    var search = function () {
        table.render({
            //表格生成的位置：#ID
            elem: '#activityTable',
            //请求地址
            url: '/manage/activity/queryActivity',
            //是否分页
            page: true,
            //请求参数
            where: {
                activityName: $.trim($("#activityName").val()),
                discountType: $.trim($("#discountType").val()),
                activityBatchno: $.trim($("#activityBatchno").val()),
                activityChnnal: $.trim($("#activityChnnal").val()),
                activityType:$.trim($("#activityType").val()),
                isOutActivity:$.trim($("#isOutActivity").val()),
                proName:$.trim($("#proName").val())
            },
            //分页信息
            request: {
                pageName: 'pageNo',
                limitName: 'pageSize'
            },
            //处理返回参数
            parseData: function (res) {
                return {
                    "code": res.code,
                    "msg": res.msg,
                    "count": res.count,
                    "data": res.data
                };
            },
            //设置返回的属性值，依据此值进行解析
            response: {
                statusName: 'code',
                statusCode: "00000",
                msgName: 'msg',
                dataName: 'data'
            },
            //每页展示的条数
            limits: [5, 10, 20],
            //每页默认显示的数量
            limit: 10,
            //单元格设置
            cols: [[
                {field: 'activityName', width: 150, title: '活动名称'},
                {field: 'activityBatchno', width: 120, title: '活动批次号'},
                {field: 'activityChnnal', width: 120, title: '活动渠道'},
                {field: 'discountType', width: 100, title: '优惠类型'},
                {field: 'activityType', title: '活动类型', width: 120},
                {field: 'proName', title: '所属项目', width: 120},
                {field: 'proId', title: '项目Id', width: 90},
                {
                    field: 'ds',
                    title: '数据统计',
                    templet: '<div><a class="iconfont icon-chakanbaogao" style="margin-left: 3%; color: #2196F3;border-radius: 5px; cursor: pointer;text-decoration: underline;" target="_blank" lay-event="showData">查看</a></div>'
                }/*,
                {
                    field: 'rs',
                    title: '报表统计',
                    templet: '<div><a class="iconfont icon-chakanbaogao" style="margin-left: 3%; color: #2196F3;border-radius: 5px; cursor: pointer;text-decoration: underline;" target="_blank" lay-event="showStats">查看</a></div>'
                }*/
            ]]
        });
    };
    //页面加载就查询列表
    search();

    //条件查询
    $("#queryBtn").on("click", function () {
        search();
    });

    //重置参数
    $("#resetBtn").on("click", function () {
        $("#activityName").val("");
        $("#discountType").val("");
        $("#activityBatchno").val("");
        $("#activityChnnal").val("");
        $("#activityType").val("");
        $("#isOutActivity").val("");
        $("#proName").val("");

    });



    //监听table行工具事件 如详情、编辑、删除操作
    table.on('tool(tableFilter)', function (obj) {
        //获取所在行的数据
        var myData = obj.data;

        //查看数据
        if (obj.event === 'showData'){
            if (myData.activityChnnal=="微信原生活动"){
                window.location.href = "/manage/acwechatdataPage?acBatchId="+myData.activityBatchno+"&activityName="+myData.activityName;
            }

            if (myData.activityChnnal=="收银宝活动"){
                window.location.href = "/manage/acallinpaydataPage?acBatchId="+myData.activityBatchno+"&activityName="+myData.activityName;
            }

        }
        //查看报表
        if (obj.event === 'showStats'){
            window.location.href = "/manage/statsRecordPage?acBatchId="+myData.activityBatchno+"&activityName="+myData.activityName;
        }
    });



    //监听form表单提交事件 防止页面跳转
    form.on('submit(formFilter)', function (data) {
        return false;
    });
    form.on('submit(queryFilter)', function (data) {
        return false;
    });

    //打开模态框
    function openModal(operateName, modalName) {
        layer.open({
            title: operateName,
            content: $('#' + modalName),
            area: ['850px', '450px'],
            //点击遮罩关闭窗口
            shadeClose: true,
            //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
            type: 1
        });
    }



});