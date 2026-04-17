package fr.bookhub.controller;

import fr.bookhub.dto.dashboard.DashboardResponse;
import fr.bookhub.dto.dashboard.RoleCreateRequest;
import fr.bookhub.dto.user.UserBasicResponse;
import fr.bookhub.service.DashboardService;
import fr.bookhub.utility.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // Dashboard pour le bibliothéquaire
    @GetMapping("/librarian")
    public DashboardResponse dashboardLibrarian() {
        return dashboardService.getLibrarianDashboard();
    }

    // Dashboard pour l'admin :
    @GetMapping("/admin")
    public List<UserBasicResponse> dashboardAdmin() {
        return dashboardService.getAdminDashboard();
    }

    // Mise à jour d'un rôle par l'admin :
    @PutMapping("/admin/role")
    public ServiceResponse<?> updateRole(@RequestBody RoleCreateRequest req) {
        return dashboardService.updateUserRole(req);
    }
}
