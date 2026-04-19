(() => {
    const getToastRoot = () => {
        let toastRoot = document.querySelector("[data-toast-root]");

        if (!toastRoot) {
            toastRoot = document.createElement("div");
            toastRoot.className = "toast-root";
            toastRoot.dataset.toastRoot = "true";
            document.body.appendChild(toastRoot);
        }

        return toastRoot;
    };

    window.toastMsg = (message, type = "success") => {
        if (!message) {
            return;
        }

        const toastRoot = getToastRoot();
        const toast = document.createElement("div");

        toast.className = `toast-message is-${type}`;
        toast.textContent = message;
        toastRoot.appendChild(toast);

        requestAnimationFrame(() => {
            toast.classList.add("is-visible");
        });

        window.setTimeout(() => {
            toast.classList.remove("is-visible");
            toast.classList.add("is-hiding");

            window.setTimeout(() => {
                toast.remove();
            }, 180);
        }, 2200);
    };

    window.setDisabled = (elements, disabled) => {
        if (!elements) {
            return;
        }

        Array.from(elements).forEach((element) => {
            element.disabled = disabled;
        });
    };
})();
