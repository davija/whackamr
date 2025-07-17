package whackamr.security;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum Permissions
{
    @FieldNameConstants.Include ADD_MERGE_REQUEST,

    @FieldNameConstants.Include ADD_PERMISSION,

    @FieldNameConstants.Include ADD_ROLE,

    @FieldNameConstants.Include ADD_ROLE_PERMISSION,

    @FieldNameConstants.Include ADD_TEAM,

    @FieldNameConstants.Include ADD_TEAM_MEMBER,

    @FieldNameConstants.Include ADD_USER,

    @FieldNameConstants.Include DELETE_MERGE_REQUEST,

    @FieldNameConstants.Include DELETE_PERMISSION,

    @FieldNameConstants.Include DELETE_ROLE,

    @FieldNameConstants.Include DELETE_TEAM,

    @FieldNameConstants.Include REMOVE_ROLE_PERMISSION,

    @FieldNameConstants.Include REMOVE_TEAM_MEMBER,

    @FieldNameConstants.Include REMOVE_USER,

    @FieldNameConstants.Include RESET_PASSWORD,

    @FieldNameConstants.Include UPDATE_MERGE_REQUEST,

    @FieldNameConstants.Include UPDATE_PERMISSION,

    @FieldNameConstants.Include UPDATE_ROLE,

    @FieldNameConstants.Include UPDATE_TEAM,

    @FieldNameConstants.Include UPDATE_USER
}
