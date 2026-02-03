# ML2++ Tutorial (Quick-Start) — Evaluating IoT-Oriented Time-Series Workflows

This README contains the **full participant tutorial** (converted from the PDF) so you can keep everything in one place. fileciteturn5file0

---

## 1) What is ML2++ (very brief)

**ML2++** is a **Model-Driven Engineering (MDE) tool** and a **domain-specific modeling language (DSL)** to build **IoT/CPS applications** that include **machine learning** and **time-series forecasting**.

From **one executable model**, ML2++ generates:
- **IoT orchestration / communication code** (ThingML-based; typically Java), and
- **ML pipeline code** (e.g., preprocessing, training, prediction, and plots; typically Python). fileciteturn5file0

---

## 2) Scenario overview: TejoFlow (IoT use case)

This tutorial uses **TejoFlow**, an **IoT river-monitoring use case** that forecasts **daily discharge** for the **Tejo River** using historical data (**1984–2023**). The goal is to show how ML2++ expresses an end-to-end workflow in a **single model**:

- **data exchange (IoT messages)**  
- **preprocessing**  
- **forecasting**  
- **decision logic** (**safe / alert**) fileciteturn5file0

### System components (ThingML)
- **SensorClient**: sends daily discharge values from **Fratel**, **Castelo de Bode**, and **Almourol**.
- **FlowServer**: forwards incoming values to analytics and returns **ack**.
- **FlowAnalytics**: runs the ML pipeline (**preprocess → train → predict**) and returns **safe()** or **alert()** based on a threshold. fileciteturn5file0

---

## 3) Part 2 — Quick-Start Editor Tasks

### 3.1 Open the editor and log in
**Task 2.4.1: Open the editor.**  
Open the ML2++ Quick-Start Editor using the URL provided by the instructor (example):  
- `https://ml2plusplus.jvmhost.net/` fileciteturn5file0

**Task 2.4.2: Log in.**  
Use credentials provided by the experimenter (examples):
- Username: `user1`  Password: `password1`
- Username: `user2`  Password: `password2` fileciteturn5file0

**Task 2.4.3: Identify the workspace elements.**  
After logging in, the Workspace contains:
- a **Model Editor** text area,
- action buttons: **Save Model**, **Show Preprocessing Plot**, **Generate Code**, **Download Code**, **Run Code**, **Show Forecasting Plots**,
- a **Console Output** panel (logs from preprocessing, generation, execution). fileciteturn5file0

**Task 2.4.4: Show preprocessing plot.**  
Click **Show Preprocessing Plot** to run preprocessing and visualize the resampled time series. Logs appear in **Console Output**. fileciteturn5file0

**Task 2.4.5: Generate code.**  
Click **Generate Code** to generate executable artifacts:
- ThingML-generated Java code (IoT orchestration/communication)
- Python scripts (preprocessing, training, prediction, plotting) fileciteturn5file0

**Task 2.4.6: Download code.**  
Click **Download Code** to obtain a `.zip` archive containing the generated files. fileciteturn5file0

---

## 4) Load the sample model

**Task 2.5.1: Clear the editor.**  
In the Model Editor, delete any existing content. fileciteturn5file0

**Task 2.5.2: Paste the sample model.**  
Copy the complete model (Section 7 of this README) into the Model Editor. From this point onward, all actions affect this model instance. fileciteturn5file0

---

## 5) Save, generate, and run

**Task 2.6.1: Save the model.**  
Click **Save Model**. A confirmation should appear. fileciteturn5file0

**Task 2.6.2: Generate code.**  
Click **Generate Code**. Confirm in **Console Output** that generation finished successfully. fileciteturn5file0

**Task 2.6.3: Run code.**  
Click **Run Code**. Inspect **Console Output** to follow preprocessing, training, prediction, and decision outputs (**safe() / alert()**). fileciteturn5file0

**Task 2.6.4: Show forecasting plots.**  
Click **Show Forecasting Plots** to visualize outputs configured in the model block. fileciteturn5file0

---

## 6) Exercise 1 — Modifying model parameters

In this exercise, you modify forecasting parameters directly in the ML2++ model and observe how the generated pipeline changes **without manually editing Python code**. fileciteturn5file0

### 6.1 Locate the model block
The `model { ... }` block belongs to `data_analytics da1`. The fragment below matches the tutorial example. fileciteturn5file0

```text
model {
  autoML OFF
  algorithm MLP mymlp(
    hidden_layer_sizes (90,90),
    input_activation relu,
    hidden_activation relu,
    output_activation relu,
    regularization l2,
    dropout 0.2,
    optimizer adam,
    rate 0.001,
    batch_size 32,
    epochs 100,
    early_stopping disable,
    overfitting_Plots TRAINING_LOSS,
    forecasting_plots FORECAST_VS_ACTUAL
  )
  training_results "data/training.txt"
}
```

### 6.2 Parameter quick reference
- `hidden_layer_sizes`: neurons per hidden layer (larger can increase capacity and training time)
- `dropout`: higher reduces overfitting but may underfit
- `regularization`: e.g., `l2`
- `optimizer`: e.g., `adam`
- `rate`: learning rate (too high may diverge; too low may train slowly)
- `batch_size`: affects stability/speed
- `epochs`: increases time and may overfit
- `early_stopping`: stop early if validation does not improve (if enabled)
- `overfitting_Plots`: training/validation diagnostic plots
- `forecasting_plots`: forecast plots (e.g., forecast vs actual) fileciteturn5file0

### 6.3 Editing and re-running
**Important:** keep attributes in the **grammar-defined order**; otherwise, the generator may map values to the wrong fields. fileciteturn5file0

**Auto-completion:**
- Windows/Linux: **Ctrl+Space**
- macOS: use your configured autocomplete shortcut if Ctrl+Space is reserved by the system fileciteturn5file0

**Task 2.7.1 (Model parameter change).**  
Using auto-completion, edit the model block to:
- switch algorithm (e.g., `MLP` → `LSTM`), and
- change one training parameter (e.g., `epochs: 100 → 10`).

Then re-run:
1. **Save Model**
2. **Generate Code**
3. **Run Code**
4. **Show Forecasting Plots**

Observe how training time and learning/forecasting curves change. fileciteturn5file0

**Task 2.7.2 (Visualization change: preprocessing plots).**  
In the `visualization` block, add one extra preprocessing plot via auto-completion, e.g.:

```text
visualization {
  plots LINE_PLOT, HISTOGRAM
}
```

Re-run the workflow (**Save → Generate Code → Run Code**). fileciteturn5file0

**Where to configure plots**
- Preprocessing plots: `visualization { plots ... }`
- Overfitting + forecasting plots: inside `model { ... }` (e.g., `overfitting_Plots`, `forecasting_plots`) fileciteturn5file0

---

## 7) Full ML2++ model instance (copy-paste into the editor)

> Note: this version includes **ACK(status : String)** to keep the tutorial wording consistent (ACK carries SAFE/ALERT). fileciteturn5file0

```thingml
datatype String<256>
@type_checker " String "
@c_type " char *"
@java_type "String"
@js_type "String"

datatype Boolean<1>
@type_checker "Boolean"
@c_type "uint8_t"
@java_type "boolean"
@js_type "boolean"

datatype Int32<4>
@type_checker "Integer"
@c_type "int32_t"
@java_type "int"
@js_type "int"

thing fragment FlowMsgs {
  // Daily discharge values (m3/s): inputs = Fratel + Castelo de Bode, target = Almourol
  message sensor_data(fratel_discharge : Int32, castelo_bode_discharge : Int32, almourol_discharge : Int32)

  // ACK now includes a status string for tutorial consistency
  message ack(status : String)

  message query(fratel_discharge : Int32, castelo_bode_discharge : Int32, almourol_discharge : Int32)
  message alert()
  message safe()
}

thing SensorClient includes FlowMsgs {
  required port data_service {
    sends sensor_data
    receives ack
  }

  // Simulated discharge values (replace with real readings in a real deployment)
  property my_fratel_discharge : Int32 = 100
  property my_castelo_bode_discharge : Int32 = 120
  property my_almourol_discharge : Int32 = 110

  statechart SensorClientBehavior init SendData {
    state SendData {
      on entry do
        print "\n==============================\n"
        print "SensorClient started (DAILY cycle)\n"
        print "In Quick-Start, the daily schedule is external.\n"
        print "==============================\n"
        print "Sending discharge packet (Fratel, Castelo de Bode, Almourol)...\n"
        data_service!sensor_data(my_fratel_discharge, my_castelo_bode_discharge, my_almourol_discharge)
      end
      transition -> ReceiveAck
      event a : data_service?ack
      action do
        print "ACK received from server. Status = "
        print a.status
        print ". Packet delivered.\n"
      end
    }

    state ReceiveAck {
      transition -> Stop
    }

    state Stop {
      on entry print "SensorClient finished.\n"
    }
  }
}

thing FlowServer includes FlowMsgs {
  provided port data_service {
    sends ack
    receives sensor_data
  }

  required port fa_service {
    sends query
    receives alert
    receives safe
  }

  property fratel_discharge : Int32
  property castelo_bode_discharge : Int32
  property almourol_discharge : Int32

  statechart FlowServerBehavior init GetData {
    state GetData {
      internal
      event e : data_service?sensor_data
      action do
        fratel_discharge = e.fratel_discharge
        castelo_bode_discharge = e.castelo_bode_discharge
        almourol_discharge = e.almourol_discharge
        print "Received packet. Forwarding to FlowAnalytics...\n"
        fa_service!query(fratel_discharge, castelo_bode_discharge, almourol_discharge)
      end
      transition -> Ack
      event fa_service?safe
      transition -> Alert
      event fa_service?alert
    }

    state Ack {
      on entry do
        print "SAFE. Sending ACK(SAFE) back to SensorClient.\n"
        data_service!ack("SAFE")
      end
      transition -> GetData
    }

    state Alert {
      on entry do
        print "ALERT. Sending ACK(ALERT) back to SensorClient.\n"
        data_service!ack("ALERT")
      end
      transition -> GetData
    }
  }
}

thing FlowAnalytics includes FlowMsgs {
  provided port fa_service {
    sends alert
    sends safe
    receives query
  }

  // Latest inputs from server
  property fratel_discharge : Int32
  property castelo_bode_discharge : Int32
  property almourol_discharge : Int32

  // 3-step prediction outputs (t+1, t+2, t+3)
  property predction1 : Int32
  property predction2 : Int32
  property predction3 : Int32

  data_analytics da1 @dalib "keras-tensorflow" {
    data {
      dataset "usecase1.csv" // Tejo River daily discharge dataset (1984/2023)
      labels ON
      features fratel_discharge,
               castelo_bode_discharge,
               almourol_discharge
      output_features almourol_discharge
      timestamps ON
      common_period_threshold 10
    }

    preprocessing {
      resample DAILY
    }

    time_series {
      sequential TRUE
      steps 3
      lag 20
      multivariate ON
      supervised_learning ON
    }

    model {
      autoML OFF
      algorithm MLP mlp_model(
        hidden_layer_sizes (90,90),
        input_activation relu,
        hidden_activation relu,
        output_activation relu,
        regularization 'l2',
        dropout 0.2,
        optimizer adam,
        rate 0.001,
        batch_size 32,
        epochs 20,
        early_stopping 'disable',
        overfitting_Plots TRAINING_LOSS,
        forecasting_plots FORECAST_VS_ACTUAL
      )
      training_results "data/training.txt"
    }

    evaluation {
      prediction_results predction1, predction2, predction3
      metrics MSE
    }

    visualization {
      plots LINE_PLOT
    }
  }

  statechart FlowAnalyticsBehavior init Preprocess {
    state Preprocess {
      on entry do
        print "Preprocessing: resample DAILY + lag/steps + supervised conversion...\n"
        da_preprocess da1
      end
      transition -> Train
    }

    state Train {
      on entry do
        print "Training MLP model...\n"
        da_train da1
      end
      transition -> Ready
    }

    state Ready {
      on entry print "Ready for prediction requests.\n"
      transition -> Predict
      event m : fa_service?query
      action do
        fratel_discharge = m.fratel_discharge
        castelo_bode_discharge = m.castelo_bode_discharge
        almourol_discharge = m.almourol_discharge
      end
    }

    state Predict {
      on entry do
        print "Predicting next 3 days for Almourol...\n"
        da_predict da1 (fratel_discharge, castelo_bode_discharge, almourol_discharge)
      end
      transition -> Decide
      on exit da_save da1
    }

    state Decide {
      on entry do
        print "Decision: checking flood threshold (150 m3/s)...\n"
        if (predction1 >= 150 or predction2 >= 150 or predction3 >= 150)
          fa_service!alert()
        else
          fa_service!safe()
      end
      transition -> Ready
    }
  }
}

configuration RiverFlowCfg @compiler "python_java" {
  instance sensorClient : SensorClient
  instance flowServer : FlowServer
  instance flowAnalytics : FlowAnalytics

  connector sensorClient.data_service => flowServer.data_service
  connector flowServer.fa_service => flowAnalytics.fa_service
}
```

---

## 8) Local execution (optional)

After clicking **Download Code** and extracting the ZIP archive: fileciteturn5file0

1) Go to the generated project directory (e.g., `python_java/`)  
2) Build:
```bash
mvn clean install
```
3) Run the fat JAR from `target/`:
```bash
java -jar <jarname>.jar
```
4) Ensure the dataset is available at the configured path (e.g., `data/usecase1.csv`), or update the `dataset "..."` directive before generating/downloading again. fileciteturn5file0
