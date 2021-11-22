
##  Endowing binary feedback: LigthFM WARP model

This subfolder contains the Python implementation of the LTR model using [lightFM library](https://making.lyst.com/lightfm/docs/home.html) as well as the CrossRec matrix used in to compute hit_rank@N metric shown in the paper.


## Environment setup
To run the tool, you need to install the following Python libraries:

 - lightfm 1.15
 - numpy 1.16.4
 - pandas 0.24.2
 - scipy 1.2.1

## Folder structure 

The folder is structured as follows:
```
 	|--- results_LTR	It stores the results for positive and negative feedback in CSV format							
	
	|--- crossrec_data.csv 	This file represents the extracted CrossRec matrix used to enable the LTR model
						
	|--- LTR_main.py   	It contains the Python scripts to train and evaluate the WARP model
	
```

## Running experiment

To replicate the experiment conducted in the paper, you have to run the **LTR_main.py** by setting the following paramenters:
- **ranks**: a list with the hit_rank thresholds used in the paper, i.e., N= 20,40,100,200,600,1000. The first element could be set to 0 and 1200 for measuring the impact of positive or negative feedback respectively
- **cutoff**: is the cutoff values used in the experiment. You can set it to 10 or 20 to replicate the experiment
- **lib_folder**: the name of the library that has to be evaluated
- **results_folder**: a path used to store the results. It has to be set according to the type of the considered feedback, cutoff and lb_folder paramenter.
- **testing_lib**: the list of the canonic name of the libraries. For instance, <em>junit:junit</em> is the canonic name of junit library


