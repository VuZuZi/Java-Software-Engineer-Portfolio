import com.backend.portfolio.dto.response.ApiResponse;
import com.backend.portfolio.dto.response.UserProfileResponse;
import com.backend.portfolio.entity.User;
import com.backend.portfolio.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;

@GetMapping("/profile")
public ApiResponse<UserProfileResponse> profile(
        @AuthenticationPrincipal UserDetails userDetails
) {

    UserRepository userService = null;
    User user = userService.findByEmail(userDetails.getUsername());

    UserProfileResponse response = new UserProfileResponse(
            user.getId(),
            user.getEmail(),
            user.getRole(),
            user.getProvider()
    );

    return
    );
}

void main() {
}

