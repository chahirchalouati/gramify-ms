package com.crcl.post.synchronizers;

import com.crcl.common.dto.queue.ImageUpload;
import com.crcl.common.dto.queue.events.QEvent;
import com.crcl.common.dto.responses.FileUploadResult;
import com.crcl.common.properties.ImageSize;
import com.crcl.post.domain.Image;
import com.crcl.post.domain.Post;
import com.crcl.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageUploadSynchronizer implements Synchronizer<ImageUpload> {
    private final PostRepository postRepository;

    @NotNull
    private static Predicate<Image> filterImageById(String imageId) {
        return image -> image.getId().equals(imageId);
    }

    @Override
    public void synchronize(QEvent<ImageUpload> event) {
        String imageId = event.getPayload().getId();
        FileUploadResult fileUploadResult = event.getPayload().getResult();
        ImageSize imageSize = event.getPayload().getSize();

        Optional<Post> post = postRepository.findByImageId(imageId);

        post.ifPresent(
                storedPost -> storedPost.getImages().stream()
                        .filter(filterImageById(imageId))
                        .findFirst()
                        .ifPresentOrElse(
                                addResizedImage(fileUploadResult, imageSize, post.get()),
                                () -> log.info("No image found for id %s".formatted(imageId))
                        ));
    }

    @NotNull
    private Consumer<Image> addResizedImage(FileUploadResult fileUploadResult,
                                            ImageSize imageSize,
                                            Post post) {
        return image -> {
            var imageToStore = new Image()
                    .setImageSize(imageSize)
                    .setParent(fileUploadResult.getEtag())
                    .setContentType(fileUploadResult.getContentType())
                    .setUrl(fileUploadResult.getLink());
            image.getProcessedImages().add(imageToStore);
            postRepository.save(post);
        };
    }
}
