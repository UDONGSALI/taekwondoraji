document.addEventListener("DOMContentLoaded", () => {
    const picker = document.querySelector("[data-month-picker]");

    if (!picker) {
        return;
    }

    const valueInput = picker.querySelector("[data-month-value]");
    const displayInput = picker.querySelector("[data-month-display]");
    const popover = picker.querySelector("[data-month-popover]");
    const selectedMonth = parseMonth(picker.getAttribute("data-selected-month"));
    const maxMonth = parseMonth(picker.getAttribute("data-max-month"));

    if (!valueInput || !displayInput || !popover || !selectedMonth || !maxMonth) {
        return;
    }

    const monthNames = [
        "1\uC6D4",
        "2\uC6D4",
        "3\uC6D4",
        "4\uC6D4",
        "5\uC6D4",
        "6\uC6D4",
        "7\uC6D4",
        "8\uC6D4",
        "9\uC6D4",
        "10\uC6D4",
        "11\uC6D4",
        "12\uC6D4",
    ];
    let visibleYear = selectedMonth.year;
    let currentValue = selectedMonth;

    const formatValue = (month) => `${month.year}-${String(month.month).padStart(2, "0")}`;
    const formatLabel = (month) => `${month.year}\uB144 ${month.month}\uC6D4`;
    const isFutureMonth = (year, month) =>
        year > maxMonth.year || (year === maxMonth.year && month > maxMonth.month);

    const closePopover = () => {
        popover.hidden = true;
    };

    const render = () => {
        const previousDisabled = visibleYear <= 2000;
        const nextDisabled = visibleYear >= maxMonth.year;

        popover.innerHTML = `
            <div class="attendance-month-popover-header">
                <button type="button" data-month-prev ${previousDisabled ? "disabled" : ""}>&lt;</button>
                <strong>${visibleYear}\uB144</strong>
                <button type="button" data-month-next ${nextDisabled ? "disabled" : ""}>&gt;</button>
            </div>
            <div class="attendance-month-grid">
                ${monthNames.map((label, index) => {
                    const month = index + 1;
                    const isSelected = visibleYear === currentValue.year && month === currentValue.month;
                    const disabled = isFutureMonth(visibleYear, month);
                    return `
                        <button type="button"
                                data-month-option="${month}"
                                class="${isSelected ? "is-selected" : ""}"
                                ${disabled ? "disabled" : ""}>
                            ${label}
                        </button>
                    `;
                }).join("")}
            </div>
        `;
    };

    const openPopover = () => {
        render();
        popover.hidden = false;
    };

    picker.addEventListener("click", (event) => {
        const previousButton = event.target.closest("[data-month-prev]");
        const nextButton = event.target.closest("[data-month-next]");
        const monthButton = event.target.closest("[data-month-option]");

        if (previousButton) {
            visibleYear -= 1;
            render();
            return;
        }

        if (nextButton) {
            visibleYear += 1;
            render();
            return;
        }

        if (monthButton) {
            currentValue = {
                year: visibleYear,
                month: Number(monthButton.getAttribute("data-month-option")),
            };
            valueInput.value = formatValue(currentValue);
            displayInput.value = formatLabel(currentValue);
            closePopover();
            return;
        }

        openPopover();
    });

    displayInput.addEventListener("focus", openPopover);

    document.addEventListener("click", (event) => {
        if (!picker.contains(event.target)) {
            closePopover();
        }
    });

    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape") {
            closePopover();
        }
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const table = document.querySelector("[data-attendance-table]");
    const modal = document.querySelector("[data-attendance-modal]");

    if (!table || !modal) {
        return;
    }

    const grid = modal.querySelector("[data-attendance-calendar-grid]");
    const title = modal.querySelector("[data-attendance-modal-title]");
    const subtitle = modal.querySelector("[data-attendance-modal-subtitle]");
    const selectedMonth = parseMonth(table.getAttribute("data-selected-month"));
    const gymId = table.getAttribute("data-gym-id");
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    if (!grid || !title || !subtitle || !selectedMonth || !gymId) {
        return;
    }

    let currentRow = null;
    let currentMemberGymMapId = null;
    let attendanceDates = new Set();
    let pendingDate = null;
    let openToken = 0;

    const formatDate = (year, month, day) =>
        `${year}-${String(month).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
    const monthLabel = (month) => `${month.year}\uB144 ${month.month}\uC6D4`;
    const daysInMonth = (month) => new Date(month.year, month.month, 0).getDate();
    const attendanceDays = (month) => {
        const totalDays = daysInMonth(month);
        const isCurrentMonth = today.getFullYear() === month.year && today.getMonth() + 1 === month.month;
        return isCurrentMonth ? Math.min(today.getDate(), totalDays) : totalDays;
    };

    const updateRowSummary = () => {
        if (!currentRow) {
            return;
        }

        const count = attendanceDates.size;
        const denominator = attendanceDays(selectedMonth);
        const rate = denominator === 0 ? 0 : Math.round((count / denominator) * 100);
        const countCell = currentRow.children[5];
        const rateBadge = currentRow.children[6]?.querySelector(".attendance-rate-badge");

        if (countCell) {
            countCell.textContent = `${count}\uD68C`;
        }

        if (rateBadge) {
            rateBadge.textContent = `${rate}%`;
        }
    };

    const renderCalendar = () => {
        const firstDate = new Date(selectedMonth.year, selectedMonth.month - 1, 1);
        const totalDays = daysInMonth(selectedMonth);
        const cells = [];

        for (let index = 0; index < firstDate.getDay(); index += 1) {
            cells.push('<span class="attendance-calendar-empty"></span>');
        }

        for (let day = 1; day <= totalDays; day += 1) {
            const dateValue = formatDate(selectedMonth.year, selectedMonth.month, day);
            const cellDate = new Date(selectedMonth.year, selectedMonth.month - 1, day);
            cellDate.setHours(0, 0, 0, 0);
            const isAttended = attendanceDates.has(dateValue);
            const isPending = pendingDate === dateValue;
            const isFuture = cellDate > today;

            cells.push(`
                <button type="button"
                        class="${isAttended ? "is-attended" : ""} ${isPending ? "is-pending" : ""}"
                        data-attendance-date="${dateValue}"
                        ${isFuture || isPending ? "disabled" : ""}>
                    <span>${day}</span>
                </button>
            `);
        }

        grid.innerHTML = cells.join("");
    };

    const openModal = async (row) => {
        const token = openToken + 1;
        openToken = token;
        currentRow = row;
        currentMemberGymMapId = row.getAttribute("data-member-gym-map-id");
        const memberName = row.getAttribute("data-member-name") || "";

        if (!currentMemberGymMapId) {
            return;
        }

        title.textContent = `${memberName} 출석 달력`;
        subtitle.textContent = monthLabel(selectedMonth);
        attendanceDates = new Set();
        pendingDate = null;
        renderCalendar();
        modal.hidden = false;
        document.body.classList.add("is-attendance-modal-open");

        const response = await fetch(
            `/api/member-gym-maps/${currentMemberGymMapId}/attendances?year=${selectedMonth.year}&month=${selectedMonth.month}`
        );
        const body = await response.json();

        if (!response.ok || body.success === false) {
            throw new Error(body.message || "\uCD9C\uC11D \uC815\uBCF4\uB97C \uBD88\uB7EC\uC624\uC9C0 \uBABB\uD588\uC2B5\uB2C8\uB2E4.");
        }

        if (token !== openToken) {
            return;
        }

        attendanceDates = new Set((body.data || []).map((item) => item.attendanceDate));
        updateRowSummary();
        renderCalendar();
    };

    const closeModal = () => {
        modal.hidden = true;
        document.body.classList.remove("is-attendance-modal-open");
        currentRow = null;
        currentMemberGymMapId = null;
        attendanceDates = new Set();
        pendingDate = null;
        openToken += 1;
    };

    const toggleAttendance = async (dateValue) => {
        if (!currentMemberGymMapId || pendingDate) {
            return;
        }

        const isAttended = attendanceDates.has(dateValue);
        pendingDate = dateValue;
        renderCalendar();

        try {
            const response = await fetch(`/gym/api/${gymId}/members/${currentMemberGymMapId}/attendances`, {
                method: isAttended ? "DELETE" : "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({attendanceDate: dateValue}),
            });
            const body = await response.json();

            if (!response.ok || body.success === false) {
                throw new Error(body.message || "\uCD9C\uC11D \uCC98\uB9AC\uC5D0 \uC2E4\uD328\uD588\uC2B5\uB2C8\uB2E4.");
            }

            if (isAttended) {
                attendanceDates.delete(dateValue);
            } else {
                attendanceDates.add(dateValue);
            }

            updateRowSummary();
        } finally {
            pendingDate = null;
            renderCalendar();
        }
    };

    table.addEventListener("click", (event) => {
        const row = event.target.closest("[data-attendance-member-row]");

        if (!row) {
            return;
        }

        openModal(row).catch((error) => {
            closeModal();
            alert(error.message);
        });
    });

    grid.addEventListener("click", (event) => {
        const button = event.target.closest("[data-attendance-date]");

        if (!button || button.disabled) {
            return;
        }

        toggleAttendance(button.getAttribute("data-attendance-date")).catch((error) => {
            alert(error.message);
        });
    });

    modal.querySelectorAll("[data-attendance-close]").forEach((button) => {
        button.addEventListener("click", closeModal);
    });

    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape" && !modal.hidden) {
            closeModal();
        }
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const openButton = document.querySelector("[data-attendance-qr-open]");
    const modal = document.querySelector("[data-attendance-qr-modal]");

    if (!openButton || !modal) {
        return;
    }

    const image = modal.querySelector("[data-attendance-qr-image]");

    const openModal = () => {
        if (image && !image.getAttribute("src")) {
            image.setAttribute("src", `${image.getAttribute("data-src")}?t=${Date.now()}`);
        }

        modal.hidden = false;
        document.body.classList.add("is-attendance-modal-open");
    };

    const closeModal = () => {
        modal.hidden = true;
        document.body.classList.remove("is-attendance-modal-open");
    };

    openButton.addEventListener("click", openModal);

    modal.querySelectorAll("[data-attendance-qr-close]").forEach((button) => {
        button.addEventListener("click", closeModal);
    });

    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape" && !modal.hidden) {
            closeModal();
        }
    });
});

const parseMonth = (value) => {
    if (!value || !/^\d{4}-\d{2}$/.test(value)) {
        return null;
    }

    const [year, month] = value.split("-").map(Number);
    return {year, month};
};
