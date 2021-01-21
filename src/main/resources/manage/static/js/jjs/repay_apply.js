$(function () {
    var $step = $("#step");
    $step.step({
        index: 0,
        time: 1000,//点亮时间
        title: ["告示", "余额查询", "兑付申请"]
    });

    $("button[name='nextStep']").on("click", function () {
        var index = $step.getIndex();
        if (index == 0) {
            $("#stepTwo").show();
            $("#stepOne").hide();
            $("#stepThree").hide();
        } else if (index == 1) {
            if ($.trim($("#instMemberNo").text()) == "--" ||
                $.trim($("#instAmount").text()) == "--") {
                layer.alert("请先查询余额信息");
                return;
            }
            $("#stepThree").show();
            $("#stepTwo").hide();
            $("#stepOne").hide();
        } else {
            return;
        }
        $step.nextStep();
    });

    $("button[name='prevStep']").on("click", function () {
        var index = $step.getIndex();
        if (index == 2) {
            $("#stepTwo").show();
            $("#stepOne").hide();
            $("#stepThree").hide();
        } else if (index == 1) {
            $("#stepOne").show();
            $("#stepTwo").hide();
            $("#stepThree").hide();
        } else {
            return;
        }
        $step.prevStep();
    });

    $("#customerFile").on("change", function () {
        var fileType = $("#customerFile").val().substr($("#customerFile").val().lastIndexOf(".") + 1);
        if (fileType != "xlsx") {
            layer.alert("只支持xlsx格式文件");
            $("#customerFile").val("");
        }
    });
});

//机构余额查询
function queryInstAmount() {
    var loading = layer.load(0, {time: 0});
    $.ajax({
        url: "/manage/jjs/amount/query",
        method: "get",
        dataType: "json",
        success: function (data) {
            layer.close(loading);
            if (data.code == "00000") {
                $("#accountName").text(data.data.accountName);
                $("#sepAcctNum").text(data.data.sepAcctNum);
                $("#instMemberNo").text(data.data.instMember);
                $("#instAmount").text(data.data.availableAmt + "元");
            } else {
                layer.alert(data.msg);
            }
        },
        error: function () {
            layer.close(loading);
            layer.alert("系统开小差，请稍后再试");
        }
    });
}

//element 展示左边菜单栏; 预加载需要使用的模块
//由于layer弹层依赖jQuery，所以可以直接得到
layui.use(['table', 'element', 'layer', 'form'], function () {
    var table = layui.table;
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;

    //抽取查询方法
    var search = function (data) {
        table.render({
            //表格生成的位置：#ID
            elem: '#repayApplyTable',
            //请求地址
            data: data,
            //是否分页
            page: true,
            //每页展示的条数
            limits: [10, 20, 50],
            //每页默认显示的数量
            limit: 10,
            //单元格设置
            cols: [[
                {field: 'hldName', width: 100, title: '姓名'},
                {field: 'cerNum', width: 200, title: '身份证号'},
                {field: 'acctNum', width: 200, title: '银行卡号'},
                {field: 'tradeAmount', width: 150, title: '金额(元)'},
                {field: 'result', width: 100, title: '验证结果'},
                {field: 'remark', title: '备注'}
            ]]
        })
    };

    $("#fileUpload").on("click", function fileUpload() {
        if ($.trim($("#customerFile").val()) == "") {
            layer.alert("请选择文件上传");
            return;
        }
        var formData = new FormData(document.getElementById("fileForm"));
        $.ajax({
            url: "/manage/jjs/customers/validate",
            type: 'post',
            contentType: false,
            processData: false,
            data: formData,
            dataType: 'json',
            success: function (data) {
                $("#customerFile").val("");
                if (data.code == "00000") {
                    if ($.trim(data.data.batchNo) != "") {
                        //可发起兑付
                        $("#repay").prop("disabled", false);
                        $("#repay").css("background-color", "#1E9FFF");
                        $("#batchNo").val(data.data.batchNo);
                    } else {
                        $("#repay").prop("disabled", true);
                        $("#repay").css("background-color", "grey");
                    }
                    search(data.data.list);
                    //备注标记为红色
                    $("tbody td[data-field='remark']").children().each(function (index, val) {
                        $(this).css("color", "red");
                    })
                } else {
                    layer.alert(data.msg);
                }
            },
            error: function () {
                $("#customerFile").val("");
                layer.alert("系统开小差，请稍后再试");
            }
        });
    });

    //发起兑付
    $("#repay").on("click", function repay() {
        var batchNo = $("#batchNo").val();
        if (!$.trim(batchNo)) {
            layer.alert("批次号为空，不可发起兑付");
            return;
        }

        layer.confirm("确认发起兑付？", function (index) {
            //动画展示，防止多次点击兑付按钮
            layer.close(index);
            var loading = layer.load(0, {time: 0});
            $.ajax({
                url: "/manage/jjs/repay/batch",
                method: "POST",
                data: {
                    "batchNo": batchNo
                },
                dataType: "json",
                success: function (data) {
                    layer.close(loading);
                    if (data.code == "00000") {
                        layer.alert("兑付申请已提交，批次号为：【" + batchNo + "】，请30分钟后在兑付记录查询菜单中查看结果。", function () {
                            location.reload();
                        });
                    } else {
                        layer.alert(data.msg);
                    }
                },
                error: function () {
                    layer.close(loading);
                    layer.alert("系统开小差，请稍后再试");
                }
            });
        });
    })
});

