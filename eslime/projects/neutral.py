version = 'v0.1.2'

# imports

import sys
import os
sys.path.append('/home/dbborens/tools/lib/python')
import elementtree.ElementTree as ET
import time
if (len(sys.argv) > 1):
	prj = sys.argv[1]
else:
	prj = ''

###############
# DEFINITIONS #
###############
instances = '500'
side = 20
founders = {1, 2, 4}

ratios = {.5, .1, .05, .01}

statePath = '/Users/dbborens/state/jeSLIME/'
scriptPath = '/Users/dbborens/github/jeSLIME/jeslime/scripts/'


#############################
# PARAMETERS -- ENVIRONMENT #
#############################
date = time.strftime('%Y-%m-%d')

def makeElement(parent, name, data):
	child = ET.SubElement(parent, name)
	child.text = data
	#parent.set(name, data)

try:
	os.mkdir(scriptPath + prj)
except:
	pass

#parent = scriptPath + prj + '/parent.sh'

n = 0

for f in founders:
	for r in ratios:
		c = int((f * 1.0) / r)

		rStr = "%2.2f" % r	
		root = ET.Element('simulation')

		
		path = statePath + '/' + prj + '/' + 'f=' + str(f) + '_r=' + rStr + '/'

		# general parameters
		general = ET.SubElement(root, 'general')

		frames = ET.SubElement(general, 'output-frames') 
		auto = ET.SubElement(frames, 'auto')
		auto.set('mode', 'decilog')

		makeElement(general, 'path', path)

		makeElement(general, 'version', version)

		makeElement(general, 'random-seed', '*')

		makeElement(general, 'instances', instances)

		makeElement(general, 'write-state-histogram', 'false')

		makeElement(general, 'write-lineage-map', 'false')

		makeElement(general, 'write-state', 'false')

		makeElement(general, 'write-metadata', 'false')

		makeElement(general, 'write-interval', 'false')

		makeElement(general, 'write-fix-time', 'true')

		makeElement(general, 'date-stamp', 'false')

		makeElement(general, 'max-step', '1000000')


		# geometry parameters
		geometry = ET.SubElement(root, 'geometry')

		makeElement(geometry, 'class', 'HexArena')

		makeElement(geometry, 'width', str(side))

		makeElement(geometry, 'height', str(side))


		################################
		# FIXED PARAMETERS - PROCESSES #
		################################

		processes = ET.SubElement(root, 'processes')

		# time advancement
		temporal = ET.SubElement(processes, 'time-process')
		makeElement(temporal, 'id','0')
		makeElement(temporal, 'class', 'SimpleGillespie')
		makeElement(temporal, 'period', '1')

		# scatter cells (IC)
		scatter = ET.SubElement(processes, 'cell-process')
		makeElement(scatter, 'id', '50')
		makeElement(scatter, 'class', 'Scatter')
		makeElement(scatter, 'period', '0')
		makeElement(scatter, 'types', '1')
		makeElement(scatter, 'tokens', str(f))

		# scatter cells (IC)
		scatter = ET.SubElement(processes, 'cell-process')
		makeElement(scatter, 'id', '100')
		makeElement(scatter, 'class', 'Scatter')
		makeElement(scatter, 'period', '0')
		makeElement(scatter, 'types', '1')
		makeElement(scatter, 'tokens', str(c))

		# bulk cell division
		divide = ET.SubElement(processes, 'cell-process')
		makeElement(divide, 'id', '200')
		makeElement(divide, 'class', 'DivideAnywhere')
		makeElement(divide, 'period', '1')

		# occasional cell swaps
		swap = ET.SubElement(processes, 'cell-process')
		makeElement(swap, 'id', '300')
		makeElement(swap, 'class', 'NeighborSwap')
		makeElement(swap, 'period', '10')

		treePath = scriptPath + '/' + prj + '/f=' + str(f) + '_r=' + rStr + '.xml'

		tree = ET.ElementTree(root)

		print(treePath)
		tree.write(treePath)

		n += 1

#pFile.close()
