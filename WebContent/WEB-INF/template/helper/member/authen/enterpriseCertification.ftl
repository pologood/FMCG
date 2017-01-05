
			<!-- 认证弹出窗 start -->
			<div id="enterpriseCertification" class="authen_busi_div" class="authen_busi_div">
				<h3 class="busi_tit">企业实名认证</h3>
				<p class="busi_txt">请按照提示填写企业真实的资料</p>
				<form id="enterpriseAutForm" action="${base}/b2b/member/authen/enterpriseCertification.jhtml" method="post">
				<input type="hidden" name="id" value="${(tenant.id)!}">
				<table class="authen_table01">
					<tr>
						<th align="right" width="140">用户账号：</th>
						<td>${tenant.telephone}</td>
					</tr>
					<tr>
						<th align="right"><span>*</span>公司名称：</th>
						<td><input type="text" name="name" class="text" value="${tenant.name}"/></td>
					</tr>
					<tr>
						<th align="right" valign="top"><span>*</span>营业执照：</th>
						<td>
							<div class="authen_upload">
								<p class="up_p01">工商注册号：<input type="text" name="licenseCode" class="text" value="${tenant.licenseCode}" /></p>
								<p class="up_p01"><input type="text" id="licensePhoto" name="licensePhoto" class="text" maxlength="200" title="${message("tenant.licensePhoto")}" />
			              			<input type="button" id="browserButton" class="authen_upload_btn" value="上传图片" />上传图片请控制在3MB以内，支持格式为bmp\jpg\png</p>
								<img class="upload_img01" src="[#if tenant.licensePhoto??]${tenant.licensePhoto}[/#if]">
							</div>
						</td>
					</tr>
					<tr>
						<th align="right"><span>*</span>法人代表：</th>
						<td><input type="text" name="legalRepr" class="text" value="${tenant.legalRepr}" /></td>
					</tr>
					<tr>
						<th align="right"><span>*</span>经营地址：</th>
						<td><span class="fieldSet">
								<input type="hidden" id="areaId" name="areaId" value="${(tenant.area.id)!}" treePath="${(tenant.area.treePath)!}" />
							</span>
						</td>
					</tr>
					<tr>
						<th align="right"></th>
						<td><input type="text" name="address" class="text"  value="${(tenant.address)!}" />
						</td>
					</tr>
					<tr>
						<th align="right">&nbsp;</th>
						<td><input class="button" type="submit" value="确定" /><input class="button" type="button" value="取消" onclick="authenLayer1();" /></td>
					</tr>
				</table>
				</form>
			</div>