package com.crcl.post.domain;

import com.crcl.post.converters.UserDtoConverter;
import com.crcl.post.dto.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Common extends BaseEntity {
    protected String username;

    @Convert(converter = UserDtoConverter.class)
    @Column(columnDefinition = "JSON")
    protected UserDto user;

    @Lob
    protected String content;

    @Enumerated(EnumType.STRING)
    protected Visibility visibility = Visibility.PRIVATE;

    public enum Visibility {
        PUBLIC, FRIEND, PRIVATE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Common common)) return false;
        if (!super.equals(o)) return false;

        if (!username.equals(common.username)) return false;
        if (!user.equals(common.user)) return false;
        if (!content.equals(common.content)) return false;
        return visibility == common.visibility;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + visibility.hashCode();
        return result;
    }
}
