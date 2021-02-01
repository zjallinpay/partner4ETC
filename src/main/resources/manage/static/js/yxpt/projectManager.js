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
        elem: '#proStarttime',
        type: 'date',
        trigger: 'click'
    });

    laydate.render({
        elem: '#proEndtime',
        type: 'date',
        trigger: 'click'
    });

 /*   laydate.render({
        elem: '#ablestartTime',
        type: 'datetime',
        trigger: 'click'
    });

    laydate.render({
        elem: '#ableendTime',
        type: 'datetime',
        trigger: 'click'
    });
*/

    //抽取查询方法
    var search = function () {
        table.render({
            //表格生成的位置：#ID
            elem: '#proTable',
            //请求地址
            url: '/manage/project/queryCondition',
            //是否分页
            page: true,
            //请求参数
            where: {
                proName: $.trim($("#proName").val())
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
                {field: 'proId', width: 90, title: '项目ID'},
                {field: 'proName', width: 150, title: '项目名称'},
                {field: 'coopOragnname', width: 150, title: '合作机构'},
                {field: 'proDepartment', width: 120, title: '项目部门'},
                {
                    field: 'ca',
                    title: '合作协议',
                    width: 120,
                    templet: '<div><a class="iconfont icon-chakanbaogao" style="margin-left: 3%; color: #2196F3;border-radius: 5px; cursor: pointer;text-decoration: underline;" target="_blank" lay-event="showAgree">查看</a></div>'
                },
                {field: 'proStarttime', title: '项目起始日期', width: 100},
                {field: 'proEndtime', title: '项目结束日期', width: 100},
                {field: 'remark', title: '备注', width: 150},
                {
                    field: 'ac',
                    title: '子活动',
                    width: 120,
                    templet: '<div><a class="iconfont icon-chakanbaogao" style="margin-left: 3%; color: #2196F3;border-radius: 5px; cursor: pointer;text-decoration: underline;" target="_blank" lay-event="showActivity">查看</a></div>'
                },
                {fixed: 'right', title: '操作', toolbar: '#operator', width: 120}
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
        $("#proName").val("");

    });




    //新增项目
    $("#addBtn").on("click", function () {
        openModal("新增项目", "addForm");
        $("#addForm").find("input[name='proName']").attr('readonly',false);
        $("#addForm").find("input[name='coopOragnname']").attr('readonly',false);
        $("#addForm").find("input[name='proDepartment']").attr('readonly',false)
        $("#addForm")[0].reset();
        form.render();
    });


    //监听table行工具事件 如详情、编辑、删除操作
    table.on('tool(tableFilter)', function (obj) {
        //获取所在行的数据
        var myData = obj.data;

        //查看协议
        if (obj.event === 'showAgree'){
            window.location.href = "/manage/connectAgreePage?proId="+myData.proId;
        }

        //查看子活动
        if (obj.event === 'showActivity'){
            window.location.href = "/manage/activityPage?proId="+myData.proId+"&proName="+myData.proName;
        }

        //编辑
        if (obj.event === 'edit') {
            openModal("编辑项目", "addForm");
            $("#addForm").find("input[name='proName']").val(myData.proName);
            $("#addForm").find("input[name='proName']").attr('readonly',true);

            $("#addForm").find("input[name='coopOragnname']").val(myData.coopOragnname);
            $("#addForm").find("input[name='coopOragnname']").attr('readonly',true);

            $("#addForm").find("input[name='proDepartment']").val(myData.proDepartment);
            $("#addForm").find("input[name='proDepartment']").attr('readonly',true)

            $("#addForm").find("input[name='proStarttime']").val(myData.proStarttime);
            $("#addForm").find("input[name='proEndtime']").val(myData.proEndtime);

            $("#addForm").find("input[name='remark']").val(myData.remark);

            $("#addForm").find("input[name='proId']").val(myData.proId);

            form.render();
        }

        //删除
        if (obj.event === 'delete') {
            var getUrl='/manage/project/deteleById?proId='+myData.proId;
            var index = layer.confirm("确定要删除吗？", function () {
                $.ajax({
                    url: getUrl,
                    type: 'get',
                    data: '',
                    dataType: 'json',
                    success: function (data) {
                        layer.close(index);
                        if (data.code == "00000") {
                            search();
                        } else {
                            layer.alert(data.msg);
                        }
                    },
                    error: function () {
                        layer.alert("操作失败，请重试！");
                    }
                });
            })
        }

    });




    //提交新增编辑表单
    form.on('submit(saveProjectFilter)',function (data) {
        window.console.info("开始处理");
        var formData = new FormData(document.getElementById("addForm"));
        var url = '/manage/project/saveOrUpdata';
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

        return false;
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

    function postFile(params){ //params是post请求需要的参数，url是请求url地址
        var url = "/manage/activity/downloadActFile";
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