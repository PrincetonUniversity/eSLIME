#!/usr/bin/Rscript

library(ggplot2)

# Parameters
basepath <- '/Users/dbborens/state/jeSLIME/again'
founders <- c(1, 2, 4)
ratios <- c(.5, .1, .05, .01)

m <- matrix(nrow=0, ncol=3)
colnames(m) <- c("founders", "ratio", "pfix")

i = 0

for (f in founders) {
	for (r in ratios) {

		i = i + 1

		# Construct file name
		filename <- sprintf('%s/f=%i_r=%2.2f/ttf.txt', basepath, f, r)
		print(filename)	

		# Load data table
		ttf.table <- read.table(filename, header = TRUE, sep="\t", na.string='NaN')

		# Calculate probability of fixation --> store in a table
		fixations = sum(ttf.table$fix_state == 1)

		row = vector(length=3)
		row[1] = f
		row[2] = r
		row[3] = fixations / nrow(ttf.table)

		print(row)
		m <- rbind(m, row)
	}
}
pdf('report.pdf')

pf.frame <- data.frame(m)
pf.frame$founders <- factor(pf.frame$founders)
print(pf.frame)

img <- qplot(pfix, ratio, data=pf.frame, colour=founders, 
	xlab="# competitors",
	ylab="Probability of mutant fixation",
	main="Probability of single mutant fixation (n=1000)")
print(img)
#img2 <- qplot(pf.frame$competitors, pf.frame$ratio, 
#	geom="line",
#	colour = pf.frame$founders,
#	xlab="# competitors",
#	ylab="Probability of mutant fixation",
#	main="Probability of single mutant fixation (n=1000)")
#print(img2)

dev.off()
