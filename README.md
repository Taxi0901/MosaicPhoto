# MosaicPhoto

reference by http://www.legildesign.com/archives/525

Processing Mosaic
This is a script I wrote inÂ Processing to emulate a photo mosaic from a set collection of images. The script collects color and brightness information from the given set of reference images and matches the best fit the the average color and brightness of a given reference point on the target image. It’s not perfect because in order to fully map all the possible fluctuation in color value and tone a huge amount of reference pictures would be necessary requiring much more computer memory than Processing is generally able to allocate. As a remedy for this, if there is no suitable match for a reference point on the target image the script takes the closest matching image from the set of reference images and tweaks its tint value to better match the reference point on the target image. Here are some examples of the output. Each of these images was created from a database of around 700-800 images.
