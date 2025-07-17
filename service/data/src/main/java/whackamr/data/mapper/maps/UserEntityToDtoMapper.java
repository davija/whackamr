package whackamr.data.mapper.maps;

import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import whackamr.data.dto.UserDto;
import whackamr.data.entity.User;

@Component
public class UserEntityToDtoMapper extends PropertyMap<User, UserDto> {

	@Override
	protected void configure() {
	}
}
