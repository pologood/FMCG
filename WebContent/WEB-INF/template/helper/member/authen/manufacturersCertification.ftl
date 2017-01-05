			<!--认证窗口-分割线-->
			<div id="manufacturersCertification" class="authen_busi_div" class="authen_busi_div">
				<h3 class="busi_tit">厂家授权认证</h3>
				<p class="busi_txt">请上传您与厂家签订的授权或代理的合同/协议，或厂家颁发给您的合法有效的授权证书，并等待后台的审核通过。</p>
				<form id="manufacturersAutForm" action="${base}/b2b/member/authen/manufacturersCertification.jhtml" method="post">
				<input type="hidden" name="id" value="${(tenant.id)!}">
				<table class="authen_table01">
					<tr>
						<th align="right" width="140">厂家授权：</th>
						<td>
							<input name="authorization" type="text" class="text" value="${tenant.authorization}" />
						</td>
					</tr>
					<tr>
						<th align="right" valign="top">&nbsp;</th>
						<td>
							<p class="up_p01"><input type="text" id="manufacturersPhoto" name="manufacturersPhoto" class="text" maxlength="200" title="${message("manufacturersPhoto")}" />
			              			<input type="button" id="browserManufacturersButton" class="authen_upload_btn" value="上传图片" />上传图片请控制在3MB以内，支持格式为bmp\jpg\png</p>
							<img class="upload_img01" src="http://image.ruishangquan.com/upload/image/201507/a1554893-c3f2-4df5-9aa1-ee6dd18b68d6.jpg">
						</td>
					</tr>
					<tr>
						<th align="right">&nbsp;</th>
						<td><input class="button" type="submit" value="确认授权" /><input onclick="authenLayer1();" class="button" type="button" value="取消授权" /></td>
					</tr>
				</table>
				</form>
			</div>
			<!-- 认证弹出窗 end -->