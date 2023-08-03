package com.lqt.util;

public final class Routing {
    public static final String BASE_URL = "/api/v1";
    public static final String LOGIN = BASE_URL + "/auth/login";
    public static final String BASE_REGISTER = "/auth/register";
    public static final String ADMIN_REGISTER = BASE_URL + BASE_REGISTER + "/admin";
    public static final String LECTURER_REGISTER = BASE_URL + BASE_REGISTER + "/lecturer";
    public static final String ALUMNI_REGISTER = BASE_URL + BASE_REGISTER + "/alumni";
    public static final String ALUMNI_PROFILE = BASE_URL + "/profile/alumni";
    public static final String ALUMNI_CONFIRM = BASE_URL + "/users/{id}/confirm";
    public static final String IS_EXIST_EMAIL = BASE_URL + "/users/exist-email";
    public static final String IS_EXIST_USERNAME = BASE_URL + "/users/exist-username";
    public static final String ALUMNI = BASE_URL + "/alumni";
    public static final String USERS = BASE_URL + "/users";
    public static final String LECTURER = BASE_URL + "/lecturers";
    public static final String UPLOAD_BG_IMG = BASE_URL + "/users/upload";
    public static final String MY_ACCOUNT = BASE_URL + "/users/me";
    public static final String USER_BY_ID = BASE_URL + "/users/{id}";
    public static final String ASSIGN_ROLE = BASE_URL + "/users/{id}/add-role";
    public static final String LECTURER_PROFILE = BASE_URL + "/profile/lecturer";
    public static final String CHANGE_PASSWORD = BASE_URL + "/users/change-password";
    public static final String FORGOT_PASSWORD = BASE_URL + "/users/forgot-password";
    public static final String RESET_PASSWORD = BASE_URL + "/users/reset-password";
    public static final String LECTURER_UPDATE_PASSWORD = BASE_URL + "/profile/lecturer/update-password";
    public static final String RESET_LECTURER_UPDATE_PASSWORD = BASE_URL + "/lecturer/{id}/reset";
    public static final String COMMENT_BY_ID = BASE_URL + "/comments/{id}";
    public static final String COMMENT_BY_POST_AND_ID = BASE_URL + "/posts/{postId}/comments/{id}";
    public static final String COMMENT_BY_COMMENT_ID = BASE_URL + "/posts/{postId}/comments/{id}/comments";
    public static final String COMMENT_BY_POST_ID = BASE_URL + "/posts/{postId}/comments";
    public static final String POST = BASE_URL + "/posts";
    public static final String POST_BY_ID = BASE_URL + "/posts/{id}";
    public static final String POST_BY_USER_ID = BASE_URL + "/users/{userId}/posts";
    public static final String LOCK_POST = BASE_URL + "/posts/{id}/lock";
    public static final String SHARE_POST = BASE_URL + "/posts/{id}/share";
    public static final String GROUP = BASE_URL + "/groups";
    public static final String GROUP_BY_ID = BASE_URL + "/groups/{id}";
    public static final String GROUP_BY_USER_ID = BASE_URL + "/groups/users/{userId}";
    public static final String ADD_TO_GROUP = BASE_URL + "/groups/{id}/users/{userId}/add";
    public static final String DELETE_TO_GROUP = BASE_URL + "/groups/{id}/users/{userId}/delete";
    public static final String MAIL_BY_GROUP_ID = BASE_URL + "/groups/{id}/mails";
    public static final String MAIL = BASE_URL + "/groups/mails";
    public static final String INTERACT = BASE_URL + "/interact/{postId}";
    public static final String QUESTION_BY_SURVEY = BASE_URL + "/surveys/{surveyId}/questions";
    public static final String QUESTION_BY_ID = BASE_URL + "/questions/{id}";
    public static final String SURVEY = BASE_URL + "/surveys";
    public static final String SURVEY_OF_POST = BASE_URL + "/posts/{postId}/surveys";
    public static final String SURVEY_BY_ID = BASE_URL + "/surveys/{surveyId}";
    public static final String SURVEY_BY_USER_ID = BASE_URL + "/users/surveys";
    public static final String RESPONSE = BASE_URL + "/survey/{surveyId}/responses";
    public static final String RESPONSE_BY_SURVEY_AND_QUESTION_ID = BASE_URL + "/survey/{surveyId}/questions/{questionId}/responses";
    public static final String RESPONSE_BY_SURVEY_AND_QUESTION_ID_AND_ID = BASE_URL + "/survey/{surveyId}/questions/{questionId}/responses/{resId}";
    /*STATISTIC*/
    public static final String COUNT_USER_OF_GROUP = BASE_URL + "/groups/{groupId}/users/count";
    public static final String COUNT_USER_OF_SYSTEM = BASE_URL + "/users/count";
    public static final String STATS_USER_OF_SYSTEM = BASE_URL + "/stats/users";
    public static final String COUNT_POST_OF_SYSTEM = BASE_URL + "/posts/count";
}
