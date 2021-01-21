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
            url: '/manage/activity/queryActMers',
            //是否分页
            page: true,
            //请求参数
            where: {
                actId: $.trim($("#actId").val())
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
                {type:'checkbox', width: 90},
                {field: 'merId', width: 90, title: '店铺ID'},
                {field: 'merName', width: 90, title: '商户名称'},
                {field: 'belongIndustry', width: 80, title: '所属行业'},
                {field: 'brandName', width: 90, title: '品牌名称'},
                {field: 'tradingArea', width: 100, title: '商圈名称'},
                {field: 'area', width: 90, title: '所属地区'},
                {field: 'isAllinpaymer', title: '商户所属', width: 90},
                {field: 'expandChannl', title: '商户拓展渠道', width: 90},
                {field: 'expandPerson', title: '商户拓展人', width: 100},
                {field: 'allinpayMerid', title: '收银宝商户号', width: 120},
                {field: 'wxpayMerid', width: 100, title: '微信子商户号'},
                {field: 'alipayMerid', title: '支付宝子商户号', width: 120},
                {field: 'cloudpayMerid', title: '云闪付子商户号', width: 90},
                {field: 'contacts', title: '联系人', width: 90},
                {field: 'contactsWay', title: '联系方式', width: 90},
                {field: 'contactsAddress', title: '联系地址', width: 90},
                {field: 'remark', title: '备注', width: 90},
                {fixed: 'right', title: '操作', toolbar: '#operator', width: 90}
            ]]
        });
    };
    //页面加载就查询列表
    search();


    //新增商户
    $("#addBtn").on("click", function () {
        openModal("新增参与商户", "addForm");
        $("#addForm")[0].reset();
        form.render();
    });


    //导出商户
    $("#batchOutputBtn").on("click", function () {
        var actId = $.trim($("#actId").val());
        console.log("actid="+actId);
        window.location.href = "/manage/activity/batchOutput?actId="+actId;

    });
    //批量导入参与商户
    layui.use(["upload"], function() {
        var upload = layui.upload;
        var actId = $.trim($("#actId").val());
        upload.render({
            elem: "#batchAddBtn",//导入id
            url: "/manage/activity/batchInsertByActId",
            data:{ actId: actId },
            field:'file',
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
        window.location.href = "/manage/merchants/downloadTemplate";
    });

    //提交新增表单
    form.on('submit(saveMerchantFilter)',function (data) {
        window.console.info("开始处理");
        var params = data.field;
        var url = '/manage/activity/insertByMerId';
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

    //监听table行工具事件 如详情、编辑、删除操作
    table.on('tool(tableFilter)', function (obj) {
        //获取所在行的数据
        var myData = obj.data;
        //删除
        if (obj.event === 'delete'){
            var actId = $.trim($("#actId").val());
            var getUrl='/manage/activity/deleteByMerId?merId='+myData.merId+"&actId="+actId;
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

});