package com.appsdevelopeblog.app.ws.ui.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.appsdevelopeblog.app.ws.io.entity.UserEntity;
import com.appsdevelopeblog.app.ws.shared.dto.UserDto;
import com.appsdevelopeblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdevelopeblog.app.ws.ui.model.response.UserRest;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDtoToUserRestMapper {
	
	UserDtoToUserRestMapper INSTANCE = Mappers.getMapper(UserDtoToUserRestMapper.class);
    
    @Mappings({
    	@Mapping(source = "addresses", target = "addressesDto")
    })
    UserDto toUserDto(UserEntity userEntity);
    List<UserDto> toUserDtoList(List<UserEntity> userDtoList);
    
    @Mappings({
    	@Mapping(source = "addressesDto", target = "addresses")
    })
    UserEntity toUserEntity(UserDto user);
    List<UserEntity> toUserEntityList(List<UserDto> user);

    @Mappings({
    	@Mapping(source = "addressesDto", target = "addresses")
    })
	UserRest toUserRest(UserDto userdto);
	List<UserRest> toUserRest(List<UserDto> userdto);
	
	 @Mappings({
	    	@Mapping(source = "addresses", target = "addressesDto")
	    })
	UserDto toUserDTOFromUserModel(UserDetailsRequestModel userDetails);
	List<UserDto> toUserDTOFromUserModelList(List<UserEntity> userDtoList);
	
}
