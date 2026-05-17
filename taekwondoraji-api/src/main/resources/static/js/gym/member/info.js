document.addEventListener("DOMContentLoaded", () => {
    const memberTable = document.querySelector("[data-gym-member-table]");

    if (!memberTable) {
        return;
    }

    const gymId = memberTable.getAttribute("data-gym-id");
    const historyTriggers = memberTable.querySelectorAll("[data-history-trigger]");
    const detailTriggers = memberTable.querySelectorAll("[data-detail-trigger]");
    const historyModal = document.querySelector("[data-history-modal]");
    const historyTitle = document.querySelector("[data-history-title]");
    const historyBody = document.querySelector("[data-history-body]");
    const historyCloseButtons = document.querySelectorAll("[data-history-close]");
    const detailModal = document.querySelector("[data-detail-modal]");
    const detailTitle = document.querySelector("[data-detail-title]");
    const detailBody = document.querySelector("[data-detail-body]");
    const detailCloseButtons = document.querySelectorAll("[data-detail-close]");
    const beltOptionSource = document.querySelector("[data-belt-option-source]");
    const statusOptionSource = document.querySelector("[data-status-option-source]");

    let currentDetail = null;

    const pointTypeLabels = {
        earn: "\uC801\uB9BD",
        use: "\uC0AC\uC6A9",
        cancel: "\uCDE8\uC18C"
    };

    const pointSourceLabels = {
        attendance: "\uCD9C\uC11D",
        goal: "\uBAA9\uD45C",
        daily_quest: "\uC77C\uC77C \uD018\uC2A4\uD2B8",
        event: "\uC774\uBCA4\uD2B8",
        manual: "\uC218\uB3D9"
    };

    const showToast = (message, type) => {
        if (typeof window.toastMsg === "function") {
            window.toastMsg(message, type);
        }
    };

    const formatDateTime = (value) => {
        if (!value) {
            return "-";
        }

        const date = new Date(value);

        if (Number.isNaN(date.getTime())) {
            return value.replace("T", " ").slice(0, 16);
        }

        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        const hour = String(date.getHours()).padStart(2, "0");
        const minute = String(date.getMinutes()).padStart(2, "0");
        return `${year}-${month}-${day} ${hour}:${minute}`;
    };

    const escapeHtml = (value) => {
        return String(value ?? "")
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;")
            .replaceAll("'", "&#039;");
    };

    const readOptions = (source) => {
        return Array.from(source?.options || []).map((option) => ({
            value: option.value,
            label: option.textContent.trim()
        }));
    };

    const beltOptions = readOptions(beltOptionSource);
    const statusOptions = readOptions(statusOptionSource);

    const renderSelect = (name, options, selectedValue) => {
        return `
            <select class="member-control-select" data-detail-field="${name}">
                ${options.map((option) => `
                    <option value="${escapeHtml(option.value)}"${option.value === selectedValue ? " selected" : ""}>
                        ${escapeHtml(option.label)}
                    </option>
                `).join("")}
            </select>
        `;
    };

    const renderEmptyHistory = (message) => {
        historyBody.innerHTML = `<p class="member-history-empty">${escapeHtml(message)}</p>`;
    };

    const openHistoryModal = (title) => {
        if (!historyModal || !historyTitle || !historyBody) {
            return;
        }

        historyTitle.textContent = title;
        historyBody.innerHTML = '<p class="member-history-empty">\uC774\uB825\uC744 \uBD88\uB7EC\uC624\uB294 \uC911\uC785\uB2C8\uB2E4.</p>';
        historyModal.hidden = false;
    };

    const closeHistoryModal = () => {
        if (historyModal) {
            historyModal.hidden = true;
        }
    };

    const closeDetailModal = () => {
        if (detailModal) {
            detailModal.hidden = true;
        }
        currentDetail = null;
    };

    const renderBeltHistories = (histories) => {
        if (!histories.length) {
            renderEmptyHistory("\uB760 \uBCC0\uACBD \uC774\uB825\uC774 \uC5C6\uC2B5\uB2C8\uB2E4.");
            return;
        }

        historyBody.innerHTML = `
            <table class="member-history-table">
                <thead>
                <tr>
                    <th>\uBCC0\uACBD \uC804</th>
                    <th>\uBCC0\uACBD \uD6C4</th>
                    <th>\uBCC0\uACBD\uC790</th>
                    <th>\uC77C\uC2DC</th>
                </tr>
                </thead>
                <tbody>
                ${histories.map((item) => `
                    <tr>
                        <td>${escapeHtml(item.beforeBeltLabel || item.beforeBeltName || "-")}</td>
                        <td>${escapeHtml(item.afterBeltLabel || item.afterBeltName || "-")}</td>
                        <td>${escapeHtml(item.changedByMemberId || "-")}</td>
                        <td>${escapeHtml(formatDateTime(item.createdAt))}</td>
                    </tr>
                `).join("")}
                </tbody>
            </table>
        `;
    };

    const renderPointHistories = (histories) => {
        if (!histories.length) {
            renderEmptyHistory("\uD3EC\uC778\uD2B8 \uC774\uB825\uC774 \uC5C6\uC2B5\uB2C8\uB2E4.");
            return;
        }

        historyBody.innerHTML = `
            <table class="member-history-table">
                <thead>
                <tr>
                    <th>\uC720\uD615</th>
                    <th>\uCD9C\uCC98</th>
                    <th>\uD3EC\uC778\uD2B8</th>
                    <th>\uC0AC\uC720</th>
                    <th>\uC77C\uC2DC</th>
                </tr>
                </thead>
                <tbody>
                ${histories.map((item) => `
                    <tr>
                        <td>${escapeHtml(item.pointTypeLabel || pointTypeLabels[item.pointType] || item.pointType || "-")}</td>
                        <td>${escapeHtml(item.pointSourceLabel || pointSourceLabels[item.pointSource] || item.pointSource || "-")}</td>
                        <td>${escapeHtml(item.point || 0)}P</td>
                        <td>${escapeHtml(item.reason || "-")}</td>
                        <td>${escapeHtml(formatDateTime(item.createdAt))}</td>
                    </tr>
                `).join("")}
                </tbody>
            </table>
        `;
    };

    const loadHistory = async (trigger) => {
        const memberGymMapId = trigger.getAttribute("data-member-gym-map-id");
        const memberName = trigger.getAttribute("data-member-name") || "\uD68C\uC6D0";
        const historyType = trigger.getAttribute("data-history-type");

        if (!gymId || !memberGymMapId || !historyType) {
            return;
        }

        const isBelt = historyType === "belt";
        const endpoint = isBelt ? "belt-histories" : "point-histories";
        openHistoryModal(`${memberName} ${isBelt ? "\uB760 \uC774\uB825" : "\uD3EC\uC778\uD2B8 \uC774\uB825"}`);

        try {
            const response = await fetch(`/gym/api/${gymId}/members/${memberGymMapId}/${endpoint}`);
            const result = await response.json();

            if (!response.ok || !result.success) {
                throw new Error(result.message || "\uC774\uB825\uC744 \uBD88\uB7EC\uC624\uC9C0 \uBABB\uD588\uC2B5\uB2C8\uB2E4.");
            }

            if (isBelt) {
                renderBeltHistories(result.data || []);
            } else {
                renderPointHistories(result.data || []);
            }
        } catch (error) {
            renderEmptyHistory(error.message || "\uC774\uB825\uC744 \uBD88\uB7EC\uC624\uC9C0 \uBABB\uD588\uC2B5\uB2C8\uB2E4.");
        }
    };

    const renderMemberDetail = (detail) => {
        currentDetail = detail;
        detailBody.innerHTML = `
            <dl class="member-detail-list">
                <div><dt>\uC774\uB984</dt><dd>${escapeHtml(detail.memberName || "-")}</dd></div>
                <div><dt>\uC5F0\uB77D\uCC98</dt><dd>${escapeHtml(detail.phoneNumber || "-")}</dd></div>
                <div><dt>\uB098\uC774</dt><dd>${escapeHtml(detail.age || "-")}</dd></div>
                <div><dt>\uC5ED\uD560</dt><dd>${escapeHtml(detail.memberRole || "-")}</dd></div>
                <div><dt>\uC0C1\uD0DC</dt><dd>${renderSelect("status", statusOptions, detail.memberStatusCode)}</dd></div>
                <div><dt>\uB760</dt><dd>${renderSelect("belt", beltOptions, detail.beltName || "white")}</dd></div>
                <div><dt>\uD3EC\uC778\uD2B8</dt><dd>${escapeHtml(detail.point || 0)}P</dd></div>
                <div><dt>\uC8FC\uC18C</dt><dd>${escapeHtml([detail.addressRoad, detail.addressDetail].filter(Boolean).join(" ") || "-")}</dd></div>
                <div><dt>\uB4F1\uB85D\uC77C\uC2DC</dt><dd>${escapeHtml(formatDateTime(detail.createdAt))}</dd></div>
            </dl>
        `;

        detailBody.querySelector('[data-detail-field="status"]')
            ?.addEventListener("change", updateDetailStatus);
        detailBody.querySelector('[data-detail-field="belt"]')
            ?.addEventListener("change", updateDetailBelt);
    };

    const openDetailModal = async (trigger) => {
        const memberGymMapId = trigger.getAttribute("data-member-gym-map-id");
        const memberName = trigger.getAttribute("data-member-name") || "\uD68C\uC6D0";

        if (!gymId || !memberGymMapId || !detailModal || !detailTitle || !detailBody) {
            return;
        }

        detailTitle.textContent = `${memberName} \uC0C1\uC138`;
        detailBody.innerHTML = '<p class="member-history-empty">\uD68C\uC6D0 \uC815\uBCF4\uB97C \uBD88\uB7EC\uC624\uB294 \uC911\uC785\uB2C8\uB2E4.</p>';
        detailModal.hidden = false;

        try {
            const response = await fetch(`/gym/api/${gymId}/members/${memberGymMapId}`);
            const result = await response.json();

            if (!response.ok || !result.success) {
                throw new Error(result.message || "\uD68C\uC6D0 \uC815\uBCF4\uB97C \uBD88\uB7EC\uC624\uC9C0 \uBABB\uD588\uC2B5\uB2C8\uB2E4.");
            }

            renderMemberDetail(result.data);
        } catch (error) {
            detailBody.innerHTML = `<p class="member-history-empty">${escapeHtml(error.message || "\uD68C\uC6D0 \uC815\uBCF4\uB97C \uBD88\uB7EC\uC624\uC9C0 \uBABB\uD588\uC2B5\uB2C8\uB2E4.")}</p>`;
        }
    };

    const updateBeltInTable = (detail) => {
        const beltTrigger = memberTable.querySelector(
            `[data-history-type="belt"][data-member-gym-map-id="${detail.memberGymMapId}"]`
        );

        if (beltTrigger) {
            beltTrigger.textContent = detail.beltLabel || detail.beltName || "-";
            beltTrigger.setAttribute("data-current-belt-name", detail.beltName || "");
        }
    };

    const updateStatusInTable = (detail) => {
        const statusCell = memberTable.querySelector(
            `[data-list-status-label][data-member-gym-map-id="${detail.memberGymMapId}"]`
        );

        if (statusCell) {
            statusCell.textContent = detail.memberStatus || "-";
        }
    };

    async function updateDetailStatus(event) {
        const field = event.currentTarget;
        const previousValue = currentDetail?.memberStatusCode || field.value;

        if (!currentDetail?.memberId) {
            return;
        }

        field.disabled = true;

        try {
            const response = await fetch(`/gym/api/${gymId}/members/${currentDetail.memberId}/status`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    memberStatus: field.value
                })
            });
            const result = await response.json();

            if (!response.ok || !result.success) {
                throw new Error(result.message || "\uC0C1\uD0DC \uBCC0\uACBD\uC5D0 \uC2E4\uD328\uD588\uC2B5\uB2C8\uB2E4.");
            }

            const selectedOption = statusOptions.find((option) => option.value === field.value);
            currentDetail = {
                ...currentDetail,
                memberStatusCode: field.value,
                memberStatus: selectedOption?.label || field.value
            };
            updateStatusInTable(currentDetail);
            showToast("\uD68C\uC6D0 \uC0C1\uD0DC\uAC00 \uC800\uC7A5\uB418\uC5C8\uC2B5\uB2C8\uB2E4.", "success");
        } catch (error) {
            field.value = previousValue;
            showToast(error.message || "\uC0C1\uD0DC \uBCC0\uACBD\uC5D0 \uC2E4\uD328\uD588\uC2B5\uB2C8\uB2E4.", "error");
        } finally {
            field.disabled = false;
        }
    }

    async function updateDetailBelt(event) {
        const field = event.currentTarget;
        const previousValue = currentDetail?.beltName || field.value;

        if (!currentDetail?.memberGymMapId) {
            return;
        }

        field.disabled = true;

        try {
            const response = await fetch(`/gym/api/${gymId}/members/${currentDetail.memberGymMapId}/belt`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    beltName: field.value
                })
            });
            const result = await response.json();

            if (!response.ok || !result.success) {
                throw new Error(result.message || "\uB760 \uBCC0\uACBD\uC5D0 \uC2E4\uD328\uD588\uC2B5\uB2C8\uB2E4.");
            }

            renderMemberDetail(result.data);
            updateBeltInTable(result.data);
            showToast("\uB760\uAC00 \uBCC0\uACBD\uB418\uC5C8\uC2B5\uB2C8\uB2E4.", "success");
        } catch (error) {
            field.value = previousValue;
            showToast(error.message || "\uB760 \uBCC0\uACBD\uC5D0 \uC2E4\uD328\uD588\uC2B5\uB2C8\uB2E4.", "error");
        } finally {
            field.disabled = false;
        }
    }

    historyTriggers.forEach((trigger) => {
        trigger.addEventListener("click", () => loadHistory(trigger));
    });

    detailTriggers.forEach((trigger) => {
        trigger.addEventListener("click", () => openDetailModal(trigger));
    });

    historyCloseButtons.forEach((button) => {
        button.addEventListener("click", closeHistoryModal);
    });

    detailCloseButtons.forEach((button) => {
        button.addEventListener("click", closeDetailModal);
    });

    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape") {
            closeHistoryModal();
            closeDetailModal();
        }
    });
});
