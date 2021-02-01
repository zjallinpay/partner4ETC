//element 展示左边菜单栏; 预加载需要使用的模块
//由于layer弹层依赖jQuery，所以可以直接得到
layui.use(['table', 'element', 'layer', 'form', 'laydate'], function () {
    var table = layui.table;
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var laydate = layui.laydate;


    //日期控件
    laydate.render({
        elem: '#reStartTime',
        type: 'date',
        trigger: 'click'
    });

    laydate.render({
        elem: '#reEndTime',
        type: 'date',
        trigger: 'click'
    });

    //抽取查询方法
    var search = function () {
        table.render({
            //表格生成的位置：#ID
            elem: '#recordTable',
            //请求地址
            url: '/manage/statsrecord/queryAll',
            //是否分页
            page: true,
            //请求参数
            where: {
                acBatchId: $.trim($("#acBatchId").val())
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
                {field: 'createTime', width: 120, title: '导出时间'},
                {field: 'reStartTime', width: 120, title: '起始时间'},
                {field: 'reEndTime', width: 120, title: '截止时间'},
                {
                    field: 'redown',
                    title: '报表下载',
                    width: 120,
                    templet: '<div><a class="iconfont icon-chakanbaogao" style="margin-left: 3%; color: #2196F3;border-radius: 5px; cursor: pointer;text-decoration: underline;" target="_blank" lay-event="downloadRecord">下载</a></div>'
                }
            ]]
        });
    };
    //页面加载就查询列表
    search();

    //生成记录
    $("#genterBtn").on("click", function () {
        var formData = new FormData(document.getElementById("queryForm"));
        console.log(JSON.stringify(formData));
        var url = '/manage/statsrecord/genateRecord';
        $.ajax({
            url: url,
            type: 'post',
            contentType: false,
            processData: false,
            data: formData,
            dataType: 'json',
            success: function (data) {
                if (data.code == "00000") {
                    layer.alert("操作成功",function () {
                        layer.closeAll();
                        search();
                    });
                } else {
                    layer.alert(data.msg);
                }
            },
            error: function () {
                layer.alert("操作失败，请重试！");
            }
        });
    });



    //监听table行工具事件
    table.on('tool(tableFilter)', function (obj) {
        //获取所在行的数据
        var myData = obj.data;

        //查看协议
        if (obj.event === 'downloadRecord'){
            window.location.href = "/manage/statsrecord/downloadRecord?reId="+myData.reId;
        }

    });






    //监听form表单提交事件 防止页面跳转
    form.on('submit(formFilter)', function (data) {
        return false;
    });
    form.on('submit(queryFilter)', function (data) {
        return false;
    });



    function postExcelFile(params){ //params是post请求需要的参数，url是请求url地址
        var url = "/manage/activitydatawechat/batchOutput";
        var form = document.createElement("form");
        form.style.display = 'none';
        form.action = url;
        form.method = "post";
        form.enctype="multipart/form-data";
        document.body.appendChild(form);
        for (var key in params) {
            var input = document.createElement("input");
            input.type = "hidden";
            input.name = key;
            input.value = params[key];
            form.appendChild(input);
        }
        form.submit();
        form.remove();
    }


});