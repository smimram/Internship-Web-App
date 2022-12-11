<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Download</title>
    <%@ include file="meta.jsp" %>
</head>
<body>
<!-- navigation bar -->
<jsp:include page="header.jsp"></jsp:include>

<div class="limiter">
    <div class="container-login100 background_style">
        <div class="wrap-login100-V2">


            <form class="login100-form validate-form p-l-55 p-r-55 p-t-178">
						<span class="login100-form-title">
							<h1>Download export of internships</h1>
						</span>
            </form>
        </div>
    </div>
</div>

<script>
    window.onload = function() {
        base64toPDF("${encodedContent}");
    }

    function base64toPDF(data) {
        var bufferArray = base64ToArrayBuffer(data);
        var blobStore = new Blob([bufferArray], { type: "text/csv" });
        if (window.navigator && window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveOrOpenBlob(blobStore);
            return;
        }
        var data = window.URL.createObjectURL(blobStore);
        var link = document.createElement('a');
        document.body.appendChild(link);
        link.href = data;
        link.download = "exportInternshipData".concat(".csv");
        link.click();
        window.URL.revokeObjectURL(data);
        link.remove();
    }
    function base64ToArrayBuffer(data) {
        var bString = window.atob(data);
        var bLength = bString.length;
        var bytes = new Uint8Array(bLength);
        for (var i = 0; i < bLength; i++) {
            var ascii = bString.charCodeAt(i);
            bytes[i] = ascii;
        }
        return bytes;
    }

</script>

</body>
</html>