package com.taekwondoraji_api.domain.gym.web.controller;

import com.taekwondoraji_api.domain.gym.web.dto.PointItemCreateForm;
import com.taekwondoraji_api.domain.gym.web.dto.PointItemInfoPage;
import com.taekwondoraji_api.domain.gym.web.service.GymPointItemCommandService;
import com.taekwondoraji_api.domain.gym.web.service.GymPointItemPageService;
import com.taekwondoraji_api.domain.pointitem.entity.PointItemStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gym/{gymId}/point-item")
public class GymPointItemPageController {

    private final GymPointItemPageService gymPointItemPageService;
    private final GymPointItemCommandService gymPointItemCommandService;

    @GetMapping("/info")
    public String info(@PathVariable Integer gymId, Model model) {
        if (!model.containsAttribute("pointItemCreateForm")) {
            model.addAttribute("pointItemCreateForm", new PointItemCreateForm());
        }

        populateModel(gymId, model);
        return "gym/point-item/info";
    }

    @PostMapping
    public String createPointItem(
            @PathVariable Integer gymId,
            @Valid @ModelAttribute("pointItemCreateForm") PointItemCreateForm pointItemCreateForm,
            BindingResult bindingResult,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            populateModel(gymId, model);
            return "gym/point-item/info";
        }

        gymPointItemCommandService.createPointItem(gymId, pointItemCreateForm, imageFile);
        redirectAttributes.addFlashAttribute("toastMessage", "포인트 아이템이 등록되었습니다.");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/gym/" + gymId + "/point-item/info";
    }

    @PostMapping("/{pointItemId}/delete")
    public String deletePointItem(
            @PathVariable Integer gymId,
            @PathVariable Integer pointItemId,
            RedirectAttributes redirectAttributes
    ) {
        gymPointItemCommandService.deletePointItem(gymId, pointItemId);
        redirectAttributes.addFlashAttribute("toastMessage", "포인트 아이템이 삭제되었습니다.");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/gym/" + gymId + "/point-item/info";
    }

    @PostMapping("/{pointItemId}/status")
    public String updatePointItemStatus(
            @PathVariable Integer gymId,
            @PathVariable Integer pointItemId,
            @RequestParam PointItemStatus itemStatus,
            RedirectAttributes redirectAttributes
    ) {
        gymPointItemCommandService.updatePointItemStatus(gymId, pointItemId, itemStatus);
        redirectAttributes.addFlashAttribute("toastMessage", "포인트 아이템 상태가 변경되었습니다.");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/gym/" + gymId + "/point-item/info";
    }

    private void populateModel(Integer gymId, Model model) {
        PointItemInfoPage pointItemInfo = gymPointItemPageService.getPointItemInfoPage(gymId);

        model.addAttribute("gymId", gymId);
        model.addAttribute("pointItemInfo", pointItemInfo);
        model.addAttribute("itemStatuses", PointItemStatus.values());
    }
}
