package com.da_chelimo.whisper.core.utils

import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

val DefaultCropContract = CropImageContractOptions(
    null,
    CropImageOptions(
        imageSourceIncludeCamera = true,
        imageSourceIncludeGallery = true,
        cropShape = CropImageView.CropShape.OVAL,
        aspectRatioX = 1,
        aspectRatioY = 1,
        fixAspectRatio = true
    )
)