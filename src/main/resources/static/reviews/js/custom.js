function cancel(reviewId) {
        var form = document.createElement("form");
        form.setAttribute("method", "post");
        form.setAttribute("action", "/reviews/" + reviewId + "/cancel");
        document.body.appendChild(form);
        form.submit();
    }