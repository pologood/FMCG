<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.article.edit")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var $inputForm = $("#inputForm");
            var $areaId = $("#areaId");
            var $addTenantImage = $("#addTenantImage");
            var $deleteTenantImage = $("a.deleteTenantImage");
            var $tenantImageTable = $("#tenantImageTable");
            var $browserButton = $("#browserButton");
            var $browserButton2 = $("#browserButton2");

            var timeout;
            var tenantImageIndex = ${(tenant.tenantImages?size)!"0"};
        [@flash_message /]
            $browserButton.browser();
            $browserButton2.browser();
            // 地区选择
            $areaId.lSelect({
                url: "${base}/common/area.jhtml"
            });

            // 表单验证
            $inputForm.validate({
                rules: {
                    articleCategoryId: "required",
                    "name": {
                        required: true
                    },
                    "tenantType": {
                        required: true
                    },
                    "areaId": {
                        required: true
                    },
                    "address": {
                        required: true
                    },
//                    "linkman": {
//                        required: true
//                    },
                    "telephone": {
                        required: true
                    }
                }
            });

            // 增加商品图片
            $addTenantImage.click(function () {
            [@compress single_line = true]
                var trHtml =
                        '<tr><td>' +
                        '<input type = "file" name = "tenantImages[' + tenantImageIndex + '].file" class = "productImageFile" \/>' +
                        '<\/td><td>' +
                        '<input type = "text" name = "tenantImages[' + tenantImageIndex + '].title" class = "text" maxlength = "200" \/>' +
                        '<\/td><td>' +
                        '<input type = "text" name = "tenantImages[' + tenantImageIndex + '].order" class = "text productImageOrder"  maxlength = "9" style = "width: 50px;" \/>' +
                        '<\/td><td>' +
                        '<a href = "javascript:;" class = "deleteTenantImage" > [${message("admin.common.delete")}] <\/a>' +
                        '<\/td><\/tr>';
            [/@compress]
                $tenantImageTable.append(trHtml);
                tenantImageIndex++;
            });

            // 删除商品图片
            $deleteTenantImage.live("click", function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("admin.dialog.deleteConfirm")}",
                    onOk: function () {
                        $this.closest("tr").remove();
                    }
                });
            });

            $("#auditRecord").on("click", function () {
                $.ajax({
                    url: "${base}/admin/tenant/audit/record.jhtml",
                    data: {id: ${tenant.id}},
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if(data.length>0){
                            var html = "<tr><th>审核结果</th><th>审核人</th><th>审核时间</th></tr>";
                            for(var i=0;i<data.length;i++){
                                html += "<tr><td>"+data[i].parameter+"</td><td>"+data[i].operator+"</td><td>"+data[i].createDate+"</td></tr>";
                            }
                            $.dialog({
                                title: "审核记录",
                                content: "<table class='list'>" + html + "</table>",
                                ok: null,
                                cancel: null
                            });
                        }else {
                            $.message("error","暂无记录");
                        }
                    }
                });
            });
        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 商家档案
</div>
<form id="inputForm" action="save.jhtml" method="post" enctype="multipart/form-data">
    <input type="hidden" name="id" value="${tenant.id}"/>
    <input type="hidden" name="template" value="${tenant.template}"/>
    <input type="hidden" name="score" value="${tenant.score}"/>
    <input type="hidden" name="totalScore" value="${tenant.totalScore}"/>
    <input type="hidden" name="price" value="${tenant.price}"/>
    <input type="hidden" name="licenseCode" class="text" value="${(tenant.licenseCode)!}"/>
    <input type="hidden" name="licensePhoto" class="text" value="${(tenant.licensePhoto)!}"/>
    <input type="hidden" name="template" class="text" value="${tenant.template}" maxlength="200"/>
    <input type="hidden" name="brokerage" class="text" value="0"/>
    <input type="hidden" name="generalize" class="text" value="0"/>

    <ul id="tab" class="tab">
        <li>
            <input type="button" value="基本信息"/>
        </li>
        <!--
        <li>
            <input type="button" value="店铺图片" />
        </li>
        -->
    </ul>
    <table class="input tabContent">
        <tr>
            <th>
                <span class="requiredField">*</span>商家名称:
            </th>
            <td>
                <input type="text" name="name" class="text" value="${(tenant.name)!}"/>
            </td>
        </tr>
        <tr>
            <th>
                logo(130*65):
            </th>
            <td>
                <span class="fieldSet">
                    <input type="text" name="logo" class="text" value="${tenant.logo}"
                           maxlength="200" title="${message("admin.product.imageTitle")}"/>
                    <input type="button" id="browserButton" class="button"
                           value="${message("admin.browser.select")}"/>
                [#if tenant.logo??]
                    <a href="${tenant.logo}" target="_blank">${message("admin.common.view")}</a>
                [/#if]
                </span>
            </td>
        </tr>
        <tr>
            <th>
                缩略图(320*320):
            </th>
            <td>
	            <span class="fieldSet">
                    <input type="text" name="thumbnail" class="text" value="${tenant.thumbnail}" maxlength="200"
                           title="${message("admin.product.imageTitle")}"/>
			        <input type="button" id="browserButton2" class="button" value="${message("admin.browser.select")}"/>
                [#if tenant.thumbnail??]
                    <a href="${tenant.thumbnail}" target="_blank">${message("admin.common.view")}</a>
                [/#if]
                </span>
            </td>
        </tr>
        <tr>
            <th>banner：</th>
            <td>
                <a href="adList.jhtml?id=${tenant.id}">商家banner图维护</a>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>商家简称:
            </th>
            <td>
                <input type="text" name="shortName" class="text" value="${(tenant.shortName)!}"/>
            </td>
        </tr>
        <tr>
            <th>
                店铺分类:
            </th>
            <td>
                <select id="tenantCategoryId" name="tenantCategoryId">
                [#list tenantCategoryTree as tenantCategory]
                    <option value="${tenantCategory.id}"[#if tenantCategory == tenant.tenantCategory]
                            selected="selected"[/#if]>
                        [#if tenantCategory.grade != 0]
                            [#list 1..tenantCategory.grade as i]
                                &nbsp;&nbsp;
                            [/#list]
                        [/#if]
                    ${tenantCategory.name}
                    </option>
                [/#list]
                </select>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>性质:
            </th>
            <td>
                <select name="tenantType">
                    <option value="">--请选择--</option>
                [#list tenantTypes as tenantType]
                    <option value="${tenantType}"
                            [#if (tenantType == (tenant.tenantType)!)]selected[/#if]>${message("admin.tenant.type."+tenantType)}</option>
                [/#list]
                </select>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>所属地区:
            </th>
            <td>
					<span class="fieldSet">
						<input type="hidden" id="areaId" name="areaId" value="${(tenant.area.id)!}"
                               treePath="${(tenant.area.treePath)!}"/>
					</span>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>地址:
            </th>
            <td>
                <input type="text" name="address" class="text" value="${(tenant.address)!}"/>
            </td>
        </tr>
        <tr>
            <th>
                联系人/法人:
            </th>
            <td>
                <input type="text" name="linkman" class="text" value="${(tenant.linkman)!}"/>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>店主:
            </th>
            <td>
                <input type="text" class="text" value="${(tenant.member.displayName)!}"/>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>联系电话:
            </th>
            <td>
                <input type="text" name="telephone" class="text" value="${(tenant.telephone)!}"/>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>状态:
            </th>
            <td>
                <select name="status">
                    <option value="none" [#if ("none" == (tenant.status)!)] selected[/#if]>待审核</option>
                    <option value="confirm" [#if ("confirm" == (tenant.status)!)] selected[/#if]>已审核</option>
                    <option value="success" [#if ("success" == (tenant.status)!)] selected[/#if]>已开通</option>
                    <option value="fail" [#if ("fail" == (tenant.status)!)] selected[/#if]>已关闭</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Article.tags")}:
            </th>
            <td>
            [#list tags as tag]
                <label>
                    <input type="checkbox" name="tagIds"
                           value="${tag.id}"[#if tenant.tags?seq_contains(tag)||tagSet?seq_contains(tag)]
                           checked="checked"[/#if]/>${tag.name}
                </label>
            [/#list]
            </td>
        </tr>
        <tr>
            <th>
                商家联盟:
            </th>
            <td>
                <label>
                    <input type="checkbox" name="isUnion" [#if tenant.isUnion] checked="checked"[/#if]/>加盟
                </label>
            </td>
        </tr>

        <tr>
            <th>
                是否自营:
            </th>
            <td>
                <label>
                    <input type="checkbox" name="isSelf" [#if tenant.isSelf] checked="checked"[/#if]/>是
                </label>
            </td>
        </tr>
       <tr>
            <th>
                联盟佣金:
            </th>
            <td>
                <input type="text" name="agency" class="text" value="${(tenant.agency)!}"/>
            </td>
        </tr>
        <!--
			<tr>
				<th>
					商家模版:
				</th>
				<td colspan="2">
					<span class="fieldSet">
						<input type="text" name="template" class="text" value="${tenant.template}" maxlength="200"/>
						<input type="button" id="browserButton" class="button" value="选择" />
						[#if tenant.template??]
							<a href="${tenant.template}" target="_blank">查看</a>
						[/#if]
					</span>
				</td>
			</tr>
			-->
        <!--
			<tr>
				<th>
					描述:
				</th>
				<td>
					<textarea id="editor" name="introduction" class="editor">${tenant.introduction?html}</textarea>
				</td>
			</tr>
			-->
    </table>
    <!--
		<table id="tenantImageTable" class="input tabContent">
			<tr>
				<td colspan="4">
					<a href="javascript:;" id="addTenantImage" class="button">添加</a>
				</td>
			</tr>
			<tr class="title">
				<th>
					文件
				</th>
				<th>
					标题
				</th>
				<th>
					排序
				</th>
				<th>
					操作
				</th>
			</tr>
			[#list tenant.tenantImages as tenantImage]
				<tr>
					<td>
						<input type="hidden" name="tenantImages[${tenantImage_index}].source" value="${tenantImage.source}" />
						<input type="hidden" name="tenantImages[${tenantImage_index}].large" value="${tenantImage.large}" />
						<input type="hidden" name="tenantImages[${tenantImage_index}].medium" value="${tenantImage.medium}" />
						<input type="hidden" name="tenantImages[${tenantImage_index}].thumbnail" value="${tenantImage.thumbnail}" />
						<input type="file" name="tenantImages[${tenantImage_index}].file" class="productImageFile ignore" />
						<a href="${tenantImage.large}" target="_blank">查看</a>
					</td>
					<td>
						<input type="text" name="tenantImages[${tenantImage_index}].title" class="text" maxlength="200" value="${tenantImage.title}" />
					</td>
					<td>
						<input type="text" name="tenantImages[${tenantImage_index}].order" class="text productImageOrder" value="${tenantImage.order}" maxlength="9" style="width: 50px;" />
					</td>
					<td>
						<a href="javascript:;" class="deleteTenantImage">[${message("admin.common.delete")}]</a>
					</td>
				</tr>
			[/#list]
		</table>
		-->
    <table class="input">
        <tr>
            <th>
                &nbsp;
            </th>
            <td>
                <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                <input type="button" class="button" value="${message("admin.common.back")}" onclick="history.go(-1)"/>

                <input type="button" class="button" value="审核记录" id="auditRecord"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>