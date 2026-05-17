document.addEventListener("DOMContentLoaded", () => {
    const memberGoalTable = document.querySelector("[data-member-goal-table]");

    if (!memberGoalTable) {
        return;
    }

    const gymId = memberGoalTable.getAttribute("data-gym-id");
    const completeButtons = memberGoalTable.querySelectorAll("[data-complete-member-goal]");
    const bulkCompleteButton = memberGoalTable.querySelector("[data-bulk-complete-member-goals]");

    const completeMemberGoal = async (button) => {
        const memberGoalId = button.getAttribute("data-member-goal-id");

        if (!gymId || !memberGoalId) {
            return;
        }

        button.disabled = true;

        try {
            const response = await fetch(`/gym/api/${gymId}/member-goals/${memberGoalId}/complete`, {
                method: "PATCH"
            });

            const result = await response.json();

            if (!response.ok || !result.success) {
                throw new Error(result.message || "완료 처리에 실패했습니다.");
            }

            if (typeof window.toastMsg === "function") {
                window.toastMsg("회원 목표가 완료 처리되었습니다.", "success");
            }

            window.setTimeout(() => {
                window.location.reload();
            }, 450);
        } catch (error) {
            button.disabled = false;

            if (typeof window.toastMsg === "function") {
                window.toastMsg(error.message || "완료 처리에 실패했습니다.", "error");
            }
        }
    };

    completeButtons.forEach((button) => {
        button.addEventListener("click", () => completeMemberGoal(button));
    });

    const completeVisibleMemberGoals = async () => {
        const memberGoalIds = Array.from(memberGoalTable.querySelectorAll("[data-complete-member-goal]"))
                .map((button) => Number(button.getAttribute("data-member-goal-id")))
                .filter((memberGoalId) => Number.isInteger(memberGoalId));

        if (memberGoalIds.length === 0) {
            if (typeof window.toastMsg === "function") {
                window.toastMsg("완료 처리할 신청 목표가 없습니다.", "info");
            }
            return;
        }

        bulkCompleteButton.disabled = true;

        try {
            const response = await fetch(`/gym/api/${gymId}/member-goals/complete-bulk`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    memberGoalIds
                })
            });

            const result = await response.json();

            if (!response.ok || !result.success) {
                throw new Error(result.message || "일괄 완료 처리에 실패했습니다.");
            }

            if (typeof window.toastMsg === "function") {
                window.toastMsg("회원 목표가 일괄 완료 처리되었습니다.", "success");
            }

            window.setTimeout(() => {
                window.location.reload();
            }, 450);
        } catch (error) {
            bulkCompleteButton.disabled = false;

            if (typeof window.toastMsg === "function") {
                window.toastMsg(error.message || "일괄 완료 처리에 실패했습니다.", "error");
            }
        }
    };

    if (bulkCompleteButton) {
        bulkCompleteButton.addEventListener("click", completeVisibleMemberGoals);
    }
});
