<script type="text/javascript">
	[#if redirectUrl??]
			location.href = "${redirectUrl}";
	[#else]
	   [#if subDomain=="box"]
			  location.href = "${base}/box/login.jhtml";
		 [#else]
			  location.href = "${base}/b2b/index.jhtml";
		 [/#if]
  [/#if]
</script>
