//element 展示左边菜单栏; 预加载需要使用的模块
//由于layer弹层依赖jQuery，所以可以直接得到
layui.use(['table', 'element', 'layer', 'form', 'laydate'], function () {
    var table = layui.table;
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;





    //抽取查询方法
    var search = function () {
        table.render({
            //表格生成的位置：#ID
            elem: '#agreeTable',
            //请求地址
            url: '/manage/agreement/queryByProId',
            //是否分页
            page: true,
            //请求参数
            where: {
                proId: $.trim($("#proId").val()),
                argName: $.trim($("#argName").val()),
                argId: $.trim($("#argId").val()),
                isExpand:$.trim($("#isExpand").val())
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
                {width: 90, type: 'checkbox'},
                {field: 'argId', width: 120, title: '协议ID'},
                {field: 'argName', width: 150, title: '协议名称'},
                {field: 'coopOrgtype', width: 120, title: '合作机构类型'},
                {field: 'createTime', width: 120, title: '添加日期'}
            ]]
        });
    };
    //页面加载就查询列表
    search();

    //这儿监听列表点击操作
    form.on('select(selectAgreeType)', function(data) {
        var value=data.value;
        if (value=='所属协议'){
            $("#isExpand").val('');
            $("#connectBtn").text('解除关联');
        }
        if (value=='关联更多协议'){
            $("#isExpand").val('true');
            $("#connectBtn").text('确认关联');
        }
        form.render();
        search();
    });

    //条件查询
    $("#queryBtn").on("click", function () {
        search();
    });


    //关联或解除关联
    $("#connectBtn").on("click", function () {
        var checkStatus = table.checkStatus('agreeTable'); //获取选中行
        var length=checkStatus.data.length;
        if (length==0){
            return;
        }
        var data={
            "proId":null,
            "argIds":[]
        };

        for (var agreement of checkStatus.data){
            data.argIds.push(agreement.argId);
        }
        data.proId=$("#proIdValue").val();

        var url = '';

        var tpye= $("#selectAgreeType").val()
        if (tpye=='所属协议'){
            url='/manage/agreement/deteleConect';
        }else {
            url='/manage/agreement/conectAgreement';
        }

        $.ajax({
            type:'post',
            url:url,
            contentType :'application/json',
            data:JSON.stringify(data),
            dataType:'json',
            success:function (res) {

                if (res.code=="00000"){
                    layer.alert("操作成功",function () {
                        layer.closeAll();
                        search();
                    });
                } else {
                    layer.alert("操作失败："+res.msg);
                }
            }
        });

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