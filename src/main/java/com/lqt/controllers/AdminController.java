package com.lqt.controllers;

import com.lqt.dto.*;
import com.lqt.pojo.Group;
import com.lqt.pojo.Role;
import com.lqt.pojo.Survey;
import com.lqt.pojo.User;
import com.lqt.service.*;
import com.lqt.util.SurveyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userDetailServiceImpl;
    @Autowired
    private GroupService groupService;
    @Autowired
    private PostService postService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private SurveyService surveyService;

    @GetMapping("/admin/login")
    public String loginAdmin(Model model, @RequestParam(value = "logout", required = false) String logout, HttpServletResponse response) {
        if (logout != null) {
            Cookie cookie = new Cookie("JWT_TOKEN", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            SecurityContextHolder.clearContext();
            model.addAttribute("success", "Logout successfully!");
            return "login";
        }
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    @PostMapping("/admin/login")
    public String processLoginAdmin(@ModelAttribute("loginDto") LoginDto loginDto,
                                    HttpServletResponse response,
                                    RedirectAttributes attributes) throws Exception {
        authenticate(loginDto.getUsername(), loginDto.getPassword());

        final UserDetails userDetails = userDetailServiceImpl
                .loadUserByUsername(loginDto.getUsername());
        User user = userDetailServiceImpl.findByUsername(userDetails.getUsername());
        List<Role> roles = userDetailServiceImpl.getAllRoleOfUser(user.getId());
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (!hasAdminRole) {
            attributes.addFlashAttribute("error", "You don not have permission to access this page");
            return "redirect:/admin/login";
        }
        JwtResponse jwtResponse = authService.login(userDetails);
        if (jwtResponse != null) {
            Cookie cookie = new Cookie("JWT_TOKEN", jwtResponse.getAccessToken());
            cookie.setPath("/");
            cookie.setMaxAge(3600);

            response.addCookie(cookie);
            return "redirect:/admin";
        } else {
            return "redirect:/admin/login";
        }
    }

    private void authenticate(String username, String password) throws Exception {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @GetMapping("/admin")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "index";
        } else {
            return "redirect:/admin/login";
        }
    }

    @GetMapping("/admin/users")
    public String getAllUsers(Model model) {
        List<UserDto> userDtos = userDetailServiceImpl.getAllUsers();
        model.addAttribute("users", userDtos);
        return "user";
    }

    @GetMapping("/admin/groups")
    public String getAllGroups(Model model, @RequestParam(value = "name", required = false, defaultValue = "") String name) {
        List<Group> groups = groupService.getAllGroups(name);
        Map<Long, Integer> numOfUsers = new HashMap<>();
        groups.forEach(g -> {
            int numOfUser = statisticService.countNumberOfUserInGroup(g.getId());
            numOfUsers.put(g.getId(), numOfUser);
        });
        model.addAttribute("groups", groups);
        model.addAttribute("numOfUsers", numOfUsers);
        return "group";
    }

    @GetMapping("/admin/groups/add")
    public String addNewGroup(Model model) {
        model.addAttribute("group", new Group());
        return "add-group";
    }


    @GetMapping("/admin/groups/{groupId}")
    public String editGroup(@PathVariable("groupId") Long groupId, Model model) {
        Group group = groupService.findById(groupId);
        model.addAttribute("group", group);
        model.addAttribute("groupId", groupId);
        return "edit-group";
    }

    @PostMapping("/admin/groups/{groupId}")
    public String executeEditGroup(@PathVariable("groupId") Long groupId, @ModelAttribute("group") Group group, RedirectAttributes attributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userDetailServiceImpl.getMyAccount(userDetails.getUsername());
            Group groupUpdated = groupService.update(group, groupId, currentUser.getId());
            if (groupUpdated != null) {
                attributes.addFlashAttribute("success", "Update group successfully!");
            } else {
                attributes.addFlashAttribute("error", "Update group failed! Please try later.");
            }
            return "redirect:/admin/groups";
        }
        attributes.addFlashAttribute("error", "Update group failed! Please try later.");
        return "redirect:/admin/groups";
    }


    @GetMapping("/admin/groups/{groupId}/members")
    public String getUsersOfGroup(@PathVariable("groupId") Long groupId, Model model) {
        List<UserDto> users = groupService.getAllUsersOfGroup(groupId);
        model.addAttribute("users", users);
        return "group-user";
    }

    @PostMapping("/admin/groups")
    public String executeAddNewGroup(RedirectAttributes attributes, @ModelAttribute("group") Group group) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userDetailServiceImpl.getMyAccount(userDetails.getUsername());
            Group groupSaved = groupService.create(group, currentUser.getId());
            if (groupSaved != null) {
                attributes.addFlashAttribute("success", "Create group successfully!");
            } else {
                attributes.addFlashAttribute("error", "Create group failed! Please try later.");
            }
            return "redirect:/admin/groups";
        }
        attributes.addFlashAttribute("error", "Create group failed! Please try later.");
        return "redirect:/admin/groups";
    }

    @GetMapping("/admin/posts")
    public String getAllPosts(Model model) {
        List<PostDto> postDtos = postService.getAllPosts();
        Map<Long, Integer> interactions = new HashMap<>();
        Map<Long, Integer> numberOfComments = new HashMap<>();
        postDtos.forEach(p -> {
            int likes = statisticService.countInteractOfPost(p.getId());
            int comments = statisticService.countCommentOfPost(p.getId());
            interactions.put(p.getId(), likes);
            numberOfComments.put(p.getId(), comments);
        });
        model.addAttribute("posts", postDtos);
        model.addAttribute("interactions", interactions);
        model.addAttribute("numberOfComments", numberOfComments);
        return "post";
    }


    @GetMapping("/admin/users/{userId}")
    public String getUserById(@PathVariable("userId") Long userId, Model model) {
        UserDto userDto = userDetailServiceImpl.findUserById(userId);
        model.addAttribute("user", userDto);
        return "user-detail";
    }

    @GetMapping("/admin/users/alumni")
    public String getAllAlumniIsNotConfirmed(Model model) {
        List<AlumniResponse> alumniResponses = userDetailServiceImpl.getAllAlumniIsNotConfirmed();
        model.addAttribute("alumniList", alumniResponses);
        return "user";
    }

    //    SURVEY
    @GetMapping("/admin/surveys")
    public String getAllSurvey(Model model) {
        List<SurveyDto> surveyDtos = surveyService.getAllSurveys();
        model.addAttribute("surveys", surveyDtos);
        return "survey";
    }

    @GetMapping("/admin/surveys/add")
    public String addNewSurvey(Model model) {
        model.addAttribute("survey", new SurveyDto());
        model.addAttribute("surveyType", SurveyType.values());
        return "add-survey";
    }

    @PostMapping("/admin/surveys")
    public String executeAddNewSurvey(RedirectAttributes attributes, @ModelAttribute("survey") SurveyDto surveyDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userDetailServiceImpl.getMyAccount(userDetails.getUsername());
            PostDto postDto1 = PostDto.builder()
                    .content("Survey")
                    .isLocked(false)
                    .isSurvey(true)
                    .build();
            PostDto postDto = postService.post(postDto1, currentUser.getId());
            SurveyDto surveyDtoSaved = surveyService.create(surveyDto, currentUser.getId(), postDto.getId());
            if (surveyDtoSaved != null) {
                attributes.addFlashAttribute("success", "Create survey successfully!");
            } else {
                attributes.addFlashAttribute("error", "Create survey failed! Please try later.");
            }
            return "redirect:/admin/surveys";
        }
        attributes.addFlashAttribute("error", "Create group failed! Please try later.");
        return "redirect:/admin/surveys";
    }

    @GetMapping("/admin/surveys/{surveyId}")
    public String editSurvey(@PathVariable("surveyId") Long surveyId, Model model) {
        SurveyDto surveyDto = surveyService.getSurveyById(surveyId);
        model.addAttribute("survey", surveyDto);
        model.addAttribute("surveyId", surveyId);
        model.addAttribute("surveyType", SurveyType.values());
        return "edit-survey";
    }

    @PostMapping("/admin/surveys/{surveyId}")
    public String executeEditSurvey(@PathVariable("surveyId") Long surveyId, @ModelAttribute("survey") SurveyDto surveyDto, RedirectAttributes attributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userDetailServiceImpl.getMyAccount(userDetails.getUsername());
            SurveyDto surveyDtoUpdated = surveyService.update(surveyDto, surveyId, currentUser.getId());
            if (surveyDtoUpdated != null) {
                attributes.addFlashAttribute("success", "Update survey successfully!");
            } else {
                attributes.addFlashAttribute("error", "Update survey failed! Please try later.");
            }
            return "redirect:/admin/surveys";
        }
        attributes.addFlashAttribute("error", "Update survey failed! Please try later.");
        return "redirect:/admin/surveys";
    }

//    Notification
    @GetMapping("/admin/send-notification")
    public String sendNotificationToAllAlumniView() {
        return "send-notification";
    }

    @GetMapping("/admin/groups/{groupId}/send-notification")
    public String sendNotificationToAllAlumniInGroupView(Model model, @PathVariable("groupId") Long groupId) {
        model.addAttribute("groupId", groupId);
        return "send-notification";
    }

}
