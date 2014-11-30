library(ggplot2)

basepath <- '/Users/dbborens/projects/T6SS_deathbirth//2014-05-07/11h39m11s/instigator_vs_pacifist/'
iterates <- 5

census.long <- data.frame(nrow=0, ncol=3)
print(colnames(census.long))
cols <- c("instance", "frame", "instigators", "pacifists")
#colnames(census.long) <- cols
census.long <- data.frame(instance=factor(), frame=numeric(), instigators=numeric(), pacifists=numeric())
print(census.long)
for (i in seq(0, iterates - 1)) {
	filename <- sprintf('%s/%i/census.txt', basepath, i)
	print(filename)
	census.table <- read.table(filename, header = TRUE, sep="\t", na.string='NaN')
    v = vector(mode = "logical", length = length(census.table))
    for (j in seq(1, length(v))) { v[j] = i }
    census.table <- cbind(v, census.table)
	colnames(census.table) <- cols
    census.long <- rbind(census.long, census.table)
}
generalParameters.frac <- census.long$pacifists / (census.long$pacifists + census.long$instigators)
#print(generalParameters.frac)

census.long <- cbind(census.long, generalParameters.frac)
qplot(frame, generalParameters.frac, data = census.long, xlab='Time (frames)', ylab='Pacifist fraction', main='Pacifist fraction', geom = c("point", "smooth"), colour=instance)
ggsave(path=basepath, file="pacifist fraction.pdf")