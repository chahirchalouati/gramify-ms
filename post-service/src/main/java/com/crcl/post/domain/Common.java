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
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Common common = (Common) o;
        return getId() != null && Objects.equals(getId(), common.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
