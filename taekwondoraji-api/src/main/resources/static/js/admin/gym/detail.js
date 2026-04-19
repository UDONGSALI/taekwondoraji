document.addEventListener("DOMContentLoaded", () => {
    const detailLayout = document.querySelector("[data-gym-id]");

    if (!detailLayout) {
        return;
    }

    const gymId = detailLayout.getAttribute("data-gym-id");
    const serviceStatusField = detailLayout.querySelector('[name="serviceStatus"]');
    const serviceStartDateField = detailLayout.querySelector('[name="serviceStartDate"]');
    const serviceEndDateField = detailLayout.querySelector('[name="serviceEndDate"]');
    const gymUpdateFields = detailLayout.querySelectorAll("[data-service-field]");

    if (!gymId || !serviceStatusField || !serviceStartDateField || !serviceEndDateField) {
        return;
    }

    const updateGym = async () => {
        if (typeof window.setDisabled === "function") {
            window.setDisabled(gymUpdateFields, true);
        }

        try {
            const response = await fetch(`/admin/api/gym/${gymId}`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    serviceStatus: serviceStatusField.value,
                    serviceStartDate: serviceStartDateField.value || null,
                    serviceEndDate: serviceEndDateField.value || null
                })
            });

            const result = await response.json();

            if (!response.ok || !result.success) {
                throw new Error(result.message || "저장에 실패했습니다.");
            }

            if (typeof window.toastMsg === "function") {
                window.toastMsg("도장 정보가 저장되었습니다.", "success");
            }
        } catch (error) {
            if (typeof window.toastMsg === "function") {
                window.toastMsg(error.message || "저장에 실패했습니다.", "error");
            }
        } finally {
            if (typeof window.setDisabled === "function") {
                window.setDisabled(gymUpdateFields, false);
            }
        }
    };

    gymUpdateFields.forEach((field) => {
        field.addEventListener("change", updateGym);
    });
});
