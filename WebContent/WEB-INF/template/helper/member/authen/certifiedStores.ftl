
			<!--认证窗口-分割线-->
			<div id="certifiedStores" class="authen_busi_div" class="authen_busi_div">
				<h3 class="busi_tit">门店实名认证</h3>
				<p class="busi_txt">请上传有清晰门店招牌的照片，等待平台验证通过</p>
				<form id="storesAutForm" action="${base}/b2b/member/authen/certifiedStores.jhtml" method="post">
				<input type="hidden" name="id" value="${(tenant.id)!}">
				<table class="authen_table01">
					<tr>
						<th align="right" valign="top" width="140"><span>*</span>门店招牌：</th>
						<td>
							<p class="up_p01"><input type="text" id="storesPhoto" name="storesPhoto" class="text" maxlength="200" title="${message("storesPhoto")}" />
			              			<input type="button" id="browserStoresButton" class="authen_upload_btn" value="上传图片" />上传图片请控制在3MB以内，支持格式为bmp\jpg\png</p>
							<img class="upload_img01" src="http://image.ruishangquan.com/upload/image/201507/a1554893-c3f2-4df5-9aa1-ee6dd18b68d6.jpg">
						</td>
					</tr>
					<tr>
						<th align="right">&nbsp;</th>
						<td><input class="button" type="submit" value="确定" /><input class="button" type="button" value="取消" onclick="authenLayer1();" /></td>
					</tr>
				</table>
				</form>
			</div>