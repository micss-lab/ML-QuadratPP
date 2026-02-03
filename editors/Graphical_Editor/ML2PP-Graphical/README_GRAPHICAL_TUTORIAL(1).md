# ML2++ Graphical Tutorial (Sirius Web)

This README is a **Markdown version** of the PDF tutorial for the **ML2++ graphical editor** (Sirius Web + Orchestrator).

---

## 1. What is ML2++?

**ML2++** is a **model-driven engineering (MDE)** environment for building **IoT/CPS applications** that include:
- **communication/orchestration** (ThingML-style components, ports, messages, connectors), and
- **data analytics / time-series forecasting** (DAML / `data_analytics` blocks that generate Python pipelines).

With one executable model, ML2++ can generate:
- IoT orchestration code (e.g., Java), and
- ML scripts (preprocess/train/predict/plots).

---

## 2. Requirements

### To run the full stack (recommended)
- **Docker**

### Optional (for local development / generators)
- **Java 11**
- **Java 21**
- **Maven**

---

## 3. Getting started

### 3.1 Clone
```bash
git clone https://github.com/micss-lab/ML-QuadratPP.git
cd ML-QuadratPP
```

> If you are working from the original upstream, you may also see references to:
> `https://github.com/ThimotyS/ML-plusplus`

### 3.2 Run with Docker
From the project root:
```bash
docker-compose build
docker-compose up
```

Then open:
```text
Orchestrator: http://localhost:8081
Sirius Web:   http://localhost:8083
```

---

## 4. Tutorial steps (Graphical Editor)

### 4.1 Uploading the Sirius Web abstract syntax (metamodel)

1. Open **Sirius Web**:
   ```text
   http://localhost:8083
   ```
2. In the top menu, go to:
   **File → Upload Project**
3. Upload the EMF project that contains the **ML2++ Ecore / Viewpoint specification**.
4. After upload:
   - Sirius Web should display the project in the **Explorer** (left panel).
   - You should see the available representations (diagrams/forms) for ML2++.

**Tip:** If the upload fails, verify that the project is the correct **EMF-ready** project and that it includes the necessary Sirius specifications.

---

### 4.2 Creating a model instance (your ML2++ model)

1. In **Explorer**, select the uploaded project.
2. Create a **new model instance** (the concrete model you will edit).
3. Sirius Web will provide multiple editors (views) for the model:
   - graphical diagrams (Things, Connections, Behaviour, Configuration)
   - textual forms for DAML/model configuration

After creation, you can open the main graphical views to define:
- datatypes + fragments,
- Things (properties/ports/messages),
- behaviours (statecharts),
- configuration (instances + connectors),
- and DAML blocks for data analytics.

---

### 4.3 Using the Orchestrator (generate / run)

1. Open the **Orchestrator**:
   ```text
   http://localhost:8081
   ```
2. Typical workflow:
   - **Generate code** from your model
   - **Run** the generated application
   - **Download** the generated project (ZIP)
   - Trigger **plots** (preprocessing / forecasting) if enabled in the model

If execution fails:
- check Orchestrator logs,
- confirm the model is valid,
- and confirm the dataset paths / dependencies match the configuration.

---

## 5. Quick overview of available Sirius Web views

| View name | Type | Element | What it is for |
|---|---|---|---|
| **ImportsDatatypesAndFragments** | Graphical | Project root | Define datatypes, objects, and fragments |
| **ThingsAndConnections** | Graphical | Project root | Define Things (properties, ports, behaviour, DAML) and connect them |
| **Behaviour** | Graphical | Thing Behaviour | Statechart flow diagram of a Thing’s behaviour |
| **Configuration** | Graphical | Configuration | Create Thing instances and connect them |
| **DA Form View** | Textual form | DAML | Configure your `data_analytics` / DAML |
| **Model Form View** | Textual form | ML model | Configure the ML model block (algorithm + parameters) |

---

## 6. Common actions for behaviour programming

Inside **action** blocks in statecharts, you typically use:

### 6.1 Variable assignment
```thingml
variable = value
```
(Types must match.)

### 6.2 If / else

**Version 1 (recommended for multiple actions):**
```thingml
if(condition) do
  ...
end
else do
  ...
end
```

**Version 2 (single action):**
```thingml
if(condition)
  ...
else do
  ...
end
```

Condition examples:
- `>`, `<`, `==`, `>=`, `<=`, logical `and/or`

### 6.3 Sending messages
```thingml
port_name!message_name(parameters)
```

### 6.4 Data analytics functions (DAML)
```thingml
da_save      da1
da_preprocess da1
da_train     da1
da_predict   da1(...)
```

---

## 7. Troubleshooting checklist

- **Sirius Web upload fails** → check EMF project structure and that you uploaded the correct folder.
- **Orchestrator cannot generate/run** → check logs, confirm model validity, confirm required runtime services are up.
- **No plots generated** → confirm your DAML/visualization settings request plots and that the run reached that stage.

---

## 8. Where to put screenshots and PDFs (recommended)

If you are organizing the repo, a common layout is:
```text
docs/
  tutorials/
    graphical/
      README.md
      tutorial.pdf
      screenshots/
    textual/
      README.md
      tutorial.pdf
      screenshots/
```

---

*End of graphical tutorial (README conversion).* 
