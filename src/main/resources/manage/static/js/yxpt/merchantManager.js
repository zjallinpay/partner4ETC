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
            elem: '#merchantTable',
            //请求地址
            url: '/manage/merchants/queryMerchant',
            //是否分页
            page: true,
            //请求参数
            where: {
                merName: $.trim($("#merName").val()),
                belongIndustry: $.trim($("#belongIndustry").val()),
                tradingArea: $.trim($("#tradingArea").val()),
                area: $.trim($("#area").val()),
                isAllinpaymer: $.trim($("#isAllinpaymer").val()),
                expandChannl: $.trim($("#expandChannl").val())
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
                {field: 'merId', width: 90, title: '店铺ID'},
                {field: 'merName', width: 200, title: '商户名称'},
                {field: 'createTime', width: 150, title: '商户添加时间'},
                {field: 'belongIndustry', width: 80, title: '所属行业'},
                {field: 'tradingArea', width: 100, title: '商圈名称'},
                {field: 'area', width: 100, title: '所属地区'},
                {field: 'isAllinpaymer', title: '商户所属', width: 90},
                {field: 'expandChannl', title: '商户拓展渠道', width: 90},
                {field: 'expandPerson', title: '商户拓展人', width: 100},
                {field: 'equipId', title: '设备ID', width: 100},
                {field: 'allinpayMerid', title: '收银宝商户号', width: 120},
                {field: 'wxpayMerid', width: 120, title: '微信子商户号'},
                {field: 'alipayMerid', title: '支付宝子商户号', width: 120},
                {field: 'cloudpayMerid', title: '云闪付子商户号', width: 90},
                {field: 'contacts', title: '联系人', width: 90},
                {field: 'contactsWay', title: '联系方式', width: 90},
                {field: 'contactsAddress', title: '联系地址', width: 200},
                {field: 'remark', title: '备注', width: 90},
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
        $("#merName").val("");
        $("#belongIndustry").val("");
        $("#tradingArea").val("");
        $("#area").val("");
        $("#isAllinpaymer").val("");
        $("#expandChannl").val("");
    });

    //批量删除
    $("#batchDetele").on("click", function () {
        var checkStatus = table.checkStatus('merchantTable'); //获取选中行
        var length=checkStatus.data.length;
        if (length==0){
            return;
        }
        var deleteData={
            deleteIds:[]
        };

        for (var merchant of checkStatus.data){
            deleteData.deleteIds.push(merchant.merId);
        }

        var index = layer.confirm("确定要删除选中的"+length+"项吗？", function () {
            var url = '/manage/merchants/batchDelete';
            $.ajax({
                type:'post',
                url:url,
                contentType :'application/json',
                data:JSON.stringify(deleteData),
                dataType:'json',
                success:function (res) {
                    layer.close(index);
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
        })


    });




    //新增商户
    $("#addBtn").on("click", function () {

        openModal("商户新增", "addForm");
        $("#addForm").find("input[name='merName']").attr('readonly',false);
        $("#addForm").find("input[name='expandPerson']").attr('readonly',false);
        $("#addForm").find("select[name='isAllinpaymer']").attr("disabled", false);
        $("#addForm")[0].reset();
        form.render();
    });


   /* //导出商户
    $("#batchOutputBtn").on("click", function () {


       /!* var datas=table.cache.merchantTable;
        var dataIds=[];
        for (var data of datas){
            dataIds.push(data.merId)
        }
        var params= {
                        'merIds':dataIds
                    };

        postExcelFile(params);*!/

    });*/

    layui.use(["upload"], function() {
        var upload = layui.upload;
        var loading =null;
        upload.render({
            elem: "#batchAddBtn",//导入id
            url: "/manage/merchants/batchImport",
            field:'multipartFile',
            size: '20480',
            accept: "file",
            exts: 'xls|xlsx',
            method:'post',
            contentType:'multipart/form-data;charset=utf-8',
            choose: function(obj){
                loading=layer.load(1, {
                    shade: false
                });
            },
            done: function (result) {
                layer.close(loading);
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
        window.location.href = "/manage/merchants/downloadTemplate";
    });

    //监听table行工具事件 如详情、编辑、删除操作
    table.on('tool(tableFilter)', function (obj) {
        //获取所在行的数据
        var myData = obj.data;
        //编辑
        if (obj.event === 'edit') {
            openModal("编辑商户", "addForm");
            $("#addForm").find("input[name='merName']").val(myData.merName);
            $("#addForm").find("input[name='merName']").attr('readonly',true);

            $("#addForm").find("input[name='belongIndustry']").val(myData.belongIndustry);
            $("#addForm").find("input[name='tradingArea']").val(myData.tradingArea);
            $("#addForm").find("select[name='area']").val(myData.area);
            $("#addForm").find("select[name='isAllinpaymer']").val(myData.isAllinpaymer);
            $("#addForm").find("select[name='isAllinpaymer']").attr("disabled", "disabled");

            $("#addForm").find("input[name='expandChannl']").val(myData.expandChannl);
            $("#addForm").find("input[name='expandPerson']").val(myData.expandPerson);
            $("#addForm").find("input[name='expandPerson']").attr('readonly',true);

            $("#addForm").find("input[name='allinpayMerid']").val(myData.allinpayMerid);
            $("#addForm").find("input[name='alipayMerid']").val(myData.alipayMerid);
            $("#addForm").find("input[name='wxpayMerid']").val(myData.wxpayMerid);
            $("#addForm").find("input[name='cloudpayMerid']").val(myData.cloudpayMerid);
            $("#addForm").find("input[name='contacts']").val(myData.contacts);
            $("#addForm").find("input[name='contactsWay']").val(myData.contactsWay);
            $("#addForm").find("input[name='contactsAddress']").val(myData.contactsAddress);
            $("#addForm").find("input[name='remark']").val(myData.remark);
            $("#addForm").find("input[name='merId']").val(myData.merId);
            $("#addForm").find("input[name='equipId']").val(myData.equipId);
            form.render();
        }

        //删除
        if (obj.event === 'delete') {
            var getUrl='/manage/merchants/deteleById?merId='+myData.merId;
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



  /*      //下载附件
        if (obj.event ==='downloadattch'){
            window.location.href = "/manage/merchants/downloadMerFile?merId="+myData.merId;
            /!*  var params={
                  'actId':myData.activityId
              };
              postFile(params);*!/
        }*/

    });

    //导出商家
    form.on('submit(outputMerchantFilter)',function (data) {

       // var url = "/manage/merchants/batchOutput";

        var formData = {
            "merName": $.trim($("#merName").val()),
            "belongIndustry": $.trim($("#belongIndustry").val()),
            "tradingArea": $.trim($("#tradingArea").val()),
            "area": $.trim($("#area").val()),
            "isAllinpaymer": $.trim($("#isAllinpaymer").val()),
            "expandChannl": $.trim($("#expandChannl").val())
        }
        window.console.info("开始处理"+JSON.stringify(formData));
        postExcelFile(formData);
    });

    //提交新增编辑表单
    form.on('submit(saveMerchantFilter)',function (data) {
        window.console.info("开始处理");

        var formData = new FormData(document.getElementById("addForm"));
        var url = '/manage/merchants/saveOrUpdata';
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

       /* var params = data.field;
        var url = '/manage/merchants/saveOrUpdata';
        $.ajax({
            type:'post',
            url:url,
            contentType :'application/json',
            data:JSON.stringify(params),
            dataType:'json',
            success:function (res) {
                window.console.info(JSON.stringify(res));
                if (res.code=="00000"){
                    layer.alert("操作成功",function () {
                        layer.closeAll();
                        search();
                    });
                } else {
                    layer.alert("操作失败："+res.msg);
                }
            }
        });*/
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

    function postExcelFile(params){ //params是post请求需要的参数，url是请求url地址
        var url = "/manage/merchants/batchOutput";
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