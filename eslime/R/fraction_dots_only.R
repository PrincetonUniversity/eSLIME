library(ggplot2)

#basepath <- '/Users/dbborens/projects/T6SS_deathbirth//2014-05-10/23h44m41s/instigator_vs_pacifist_periodic_movie/'
#basepath <- '/Users/dbborens/projects/T6SS/simplified/2014-05-15/14h06m13s/IvP/'
#basepath <- '/Users/dbborens/projects/T6SS/simplified/2014-05-15/14h07m48s/IvI/'
#basepath <- '/Users/dbborens/projects/T6SS/simplified/2014-05-15/14h04m12s/PvP/'
basepath <- '/Users/dbborens/projects/T6SS/simplified//2014-05-29/IvPm_re_halt/10k/delta_5.0/21h46m08s'
#basepath <- '/Users/dbborens/projects/T6SS/simplified/2014-05-29/IvPm_re/10k/delta_0.0/16h38m49s'
#basepath <- '/Users/dbborens/projects/T6SS_deathbirth//2014-05-10/23h45m07s/instigator_vs_pacifist_arena_movie/'
#basepath <- '/Users/dbborens/projects/T6SS_deathbirth//2014-05-10/23h42m58s/instigator_vs_pacifist_arena/'
#basepath <- '/Users/dbborens/projects/T6SS_deathbirth//2014-05-10/23h43m18s/instigator_vs_pacifist_periodic/'
iterates <- 10

census.long <- data.frame(nrow=0, ncol=3)
cols <- c("instance", "frame", "instigators", "pacifists")
#colnames(census.long) <- cols
census.long <- data.frame(instance=factor(), frame=numeric(), instigators=numeric(), pacifists=numeric())
for (i in seq(0, iterates - 1)) {
	filename <- sprintf('%s/%i/census.txt', basepath, i)
	print(filename)
	census.table <- read.table(filename, header = TRUE, sep="\t", na.string='NaN')
    v = vector(mode = "character", length = nrow(census.table))
    for (j in seq(1, nrow(census.table))) { v[j] = i }
    census.table <- cbind(v, census.table)
	colnames(census.table) <- cols
    census.long <- rbind(census.long, census.table)
}
generalParameters.frac <- census.long$instigators / (census.long$pacifists + census.long$instigators)
#print(generalParameters.frac)

census.long <- cbind(census.long, generalParameters.frac)
pdf(sprintf("%s/plots.pdf", basepath), onefile = TRUE)
aggdata <- aggregate(census.long$generalParameters.frac, list(time=census.long$frame), mean)
head(aggdata)
print(qplot(frame, generalParameters.frac, data = aggdata, xlab='Time (frames)', ylab='Average blue fraction', main='Blue fraction', geom = c("line")))
print(qplot(frame, generalParameters.frac, data = census.long, xlab='Time (frames)', ylab='Blue fraction', main='Blue fraction', geom = c("line"), facets=instance ~ .))
print(qplot(frame, pacifists, data = census.long, xlab='Time (frames)', ylab='Red population', main='Red population', geom = c("line"), facets=instance ~ .))
print(qplot(frame, instigators, data = census.long, xlab='Time (frames)', ylab='Blue population', main='Blue population', geom = c("line"), facets=instance ~ .))
dev.off()

 #ggsave(path=basepath, file="plots.pdf")