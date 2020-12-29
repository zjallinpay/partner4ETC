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
        elem: '#startTime',
        type: 'date',
        trigger: 'click'
    });

    laydate.render({
        elem: '#endTime',
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
                coopOrgan: $.trim($("#coopOrgan").val()),
                fundType: $.trim($("#fundType").val())
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
                {field: 'activityId', width: 90, title: '活动ID'},
                {field: 'activityName', width: 90, title: '活动名称'},
                {field: 'activityChnnal', width: 80, title: '活动渠道'},
                {field: 'activityBatchno', width: 90, title: '活动批次号'},
                {field: 'discountType', width: 100, title: '优惠类型'},
                {field: 'coopOrgan', width: 90, title: '合作机构'},
                {field: 'startTime', title: '活动开始时间', width: 90},
                {field: 'endTime', title: '活动结束时间', width: 90},
                {field: 'fundType', title: '资金模式', width: 100},
                {field: 'activityMaster', title: '活动拓展人', width: 120},
                {
                    field: 'zk',
                    title: '参与商户',
                    templet: '<div><a class="iconfont icon-chakanbaogao" style="margin-left: 3%; color: #2196F3;border-radius: 5px; cursor: pointer;text-decoration: underline;" target="_blank" lay-event="showMers">查看</a></div>'
                },
                {
                    field: 'activityFile',
                    title: '附件材料',
                    templet: '<div>{{# if( d.activityFile == null){ }} 无    {{# } else { }}<a class="iconfont icon-chakanbaogao" style="margin-left: 3%; color: #2196F3;border-radius: 5px; cursor: pointer;text-decoration: underline;" target="_blank" lay-event="downloadattch">  查看 </a>{{# } }}</div>'
                },
                {field: 'activityStatus', title: '活动状态', width: 120},
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
        $("#activityName").val("");
        $("#activityBatchno").val("");
        $("#coopOrgan").val("");
        $("#discountType").val("");
        $("#activityChnnal").val("");
        $("#fundType").val("");
    });



    //新增商户
    $("#addBtn").on("click", function () {
        openModal("新增活动", "addForm");

        $("#addForm").find("input[name='activityName']").attr('readonly',false);

        $("#addForm").find("select[name='activityChnnal']").attr("disabled", false);

        $("#addForm").find("input[name='activityBatchno']").attr('readonly',false);

        $("#addForm").find("select[name='discountType']").attr("disabled", false);

        $("#addForm").find("select[name='fundType']").attr("disabled", false);

        $("#addForm").find("input[name='activityMaster']").attr('readonly',false);

        $("#addForm").find("input[name='activity_file']").show();
        $("#activityFile").hide();

        $("#addForm").find("input[name='startTime']").attr('readonly',false);

        $("#addForm").find("input[name='endTime']").attr('readonly',false);

        $("#addForm").find("input[name='ablestartTime']").attr('readonly',false);

        $("#addForm").find("input[name='ableendTime']").attr('readonly',false);

        $("#addForm").find("input[name='ableWeek']").attr('readonly',false);

        $("#addForm").find("input[name='bankLimit']").attr('readonly',false);

        $("#addForm").find("input[name='coopOrgan']").attr('readonly',false);

        $("#addForm").find("textarea[name='activityRemark']").attr('readonly',false);
        $("#addSubmit").show();
        $("#editbtn").hide();

        $("#addForm")[0].reset();
        form.render();
    });





    //批导活动
    layui.use(["upload"], function() {
        var upload = layui.upload;
        upload.render({
            elem: "#batchAddBtn",//导入id
            url: "/manage/activity/batchImport",
            field:'multipartFile',
            size: '2048',
            accept: "file",
            exts: 'xls|xlsx',
            method:'post',
            contentType:'multipart/form-data;charset=utf-8',
            done: function (result) {
                if (result.code == "00000") {
                    layer.alert("批导成功，共"+result.data+"条。");
                } else {
                    layer.alert("批导失败："+result.msg);
                }
                search();
            }
        });
    });


    //下载模板
    $("#downloadTemplate").on("click", function () {
        window.location.href = "/manage/activity/downloadTemplate";
    });

    //监听table行工具事件 如详情、编辑、删除操作
    table.on('tool(tableFilter)', function (obj) {
        //获取所在行的数据
        var myData = obj.data;

        //查看参与商户
        if (obj.event === 'showMers'){
            window.location.href = "/manage/activityMer/actMersPage?actId="+myData.activityId;

        }
        //下载附件
        if (obj.event ==='downloadattch'){
            window.location.href = "/manage/activity/downloadActFile?actId="+myData.activityId;
          /*  var params={
                'actId':myData.activityId
            };
            postFile(params);*/
        }
        //查看
        if (obj.event === 'watch') {
            openModal("活动详情", "addForm");
            $("#addForm").find("input[name='activityName']").attr('readonly',true);
            $("#addForm").find("input[name='activityName']").val(myData.activityName);

            $("#addForm").find("select[name='activityChnnal']").attr("disabled", "disabled");
            $("#addForm").find("select[name='activityChnnal']").val(myData.activityChnnal);

            $("#addForm").find("input[name='activityBatchno']").attr('readonly',true);
            $("#addForm").find("input[name='activityBatchno']").val(myData.activityBatchno);

            $("#addForm").find("select[name='discountType']").attr("disabled", "disabled");
            $("#addForm").find("select[name='discountType']").val(myData.discountType);

            $("#addForm").find("select[name='fundType']").attr("disabled", "disabled");
            $("#addForm").find("select[name='fundType']").val(myData.fundType);

            $("#addForm").find("input[name='activityMaster']").attr('readonly',true);
            $("#addForm").find("input[name='activityMaster']").val(myData.activityMaster);

            $("#addForm").find("input[name='activity_file']").hide();
            //$("#addForm").find("input[name='activityFile']").attr('readonly',true);
            $("#activityFile").text(myData.activityFile);
            $("#activityFile").show();
            $("#activityFile").attr('href','/manage/activity/downloadActFile?actId='+myData.activityId);

            $("#addForm").find("input[name='startTime']").attr('readonly',true);
            $("#addForm").find("input[name='startTime']").val(myData.startTime);

            $("#addForm").find("input[name='endTime']").attr('readonly',true);
            $("#addForm").find("input[name='endTime']").val(myData.endTime);

            $("#addForm").find("input[name='ablestartTime']").attr('readonly',true);
            $("#addForm").find("input[name='ablestartTime']").val(myData.ablestartTime);

            $("#addForm").find("input[name='ableendTime']").attr('readonly',true);
            $("#addForm").find("input[name='ableendTime']").val(myData.ableendTime);

            $("#addForm").find("input[name='ableWeek']").attr('readonly',true);
            $("#addForm").find("input[name='ableWeek']").val(myData.ableWeek);

            $("#addForm").find("input[name='bankLimit']").attr('readonly',true);
            $("#addForm").find("input[name='bankLimit']").val(myData.bankLimit);

            $("#addForm").find("input[name='coopOrgan']").attr('readonly',true);
            $("#addForm").find("input[name='coopOrgan']").val(myData.coopOrgan);

            $("#addForm").find("textarea[name='activityRemark']").attr('readonly',true);
            $("#addForm").find("textarea[name='activityRemark']").val(myData.activityRemark);

            $("#addForm").find("input[name='activityId']").val(myData.activityId);

            $("#addSubmit").hide();
            $("#editbtn").show();
            form.render();
        }

        //删除
        if (obj.event === 'delete') {
            var getUrl='/manage/activity/deteleById?actId='+myData.activityId;
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
    form.on('submit(saveActivityFilter)',function (data) {
        window.console.info("开始处理");
        var formData = new FormData(document.getElementById("addForm"));
        var url = '/manage/activity/saveOrUpdata';
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


    //编辑表单
    form.on('submit(editActivityFilter)',function (data) {
        var mydata=data.field;
        $("#addForm").find("input[name='activityName']").attr('readonly',false);
        $("#addForm").find("input[name='activityName']").val(mydata.activityName);

        $("#addForm").find("select[name='activityChnnal']").attr("disabled",false);
        $("#addForm").find("select[name='activityChnnal']").val(mydata.activityChnnal);

        $("#addForm").find("input[name='activityBatchno']").attr('readonly',false);
        $("#addForm").find("input[name='activityBatchno']").val(mydata.activityBatchno);

        $("#addForm").find("select[name='discountType']").attr("disabled",false);
        $("#addForm").find("select[name='discountType']").val(mydata.discountType);

        $("#addForm").find("select[name='fundType']").attr("disabled",false);
        $("#addForm").find("select[name='fundType']").val(mydata.fundType);

        $("#addForm").find("input[name='activityMaster']").attr('readonly',false);
        $("#addForm").find("input[name='activityMaster']").val(mydata.activityMaster);

        $("#addForm").find("input[name='activity_file']").show();
        $("#activityFile").hide();
       // $("#addForm").find("input[name='activity_file']").val(mydata.activityFile);

        $("#addForm").find("input[name='startTime']").attr('readonly',false);
        $("#addForm").find("input[name='startTime']").val(mydata.startTime);

        $("#addForm").find("input[name='endTime']").attr('readonly',false);
        $("#addForm").find("input[name='endTime']").val(mydata.endTime);

        $("#addForm").find("input[name='ablestartTime']").attr('readonly',false);
        $("#addForm").find("input[name='ablestartTime']").val(mydata.ablestartTime);

        $("#addForm").find("input[name='ableendTime']").attr('readonly',false);
        $("#addForm").find("input[name='ableendTime']").val(mydata.ableendTime);

        $("#addForm").find("input[name='ableWeek']").attr('readonly',false);
        $("#addForm").find("input[name='ableWeek']").val(mydata.ableWeek);

        $("#addForm").find("input[name='bankLimit']").attr('readonly',false);
        $("#addForm").find("input[name='bankLimit']").val(mydata.bankLimit);

        $("#addForm").find("input[name='coopOrgan']").attr('readonly',false);
        $("#addForm").find("input[name='coopOrgan']").val(mydata.coopOrgan);

        $("#addForm").find("textarea[name='activityRemark']").attr('readonly',false);
        $("#addForm").find("textarea[name='activityRemark']").val(mydata.activityRemark);

        $("#addForm").find("input[name='activityId']").val(mydata.activityId);

        $("#addSubmit").show();
        $("#editbtn").hide();
        form.render();

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